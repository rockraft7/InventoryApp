package com.faizalsidek.inventory.web.rest;

import com.faizalsidek.inventory.Application;
import com.faizalsidek.inventory.domain.ItemStatus;
import com.faizalsidek.inventory.repository.ItemStatusRepository;
import com.faizalsidek.inventory.repository.search.ItemStatusSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ItemStatusResource REST controller.
 *
 * @see ItemStatusResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ItemStatusResourceIntTest {

    private static final String DEFAULT_TAG = "AAAAA";
    private static final String UPDATED_TAG = "BBBBB";
    private static final String DEFAULT_CAPTION = "AAAAA";
    private static final String UPDATED_CAPTION = "BBBBB";

    @Inject
    private ItemStatusRepository itemStatusRepository;

    @Inject
    private ItemStatusSearchRepository itemStatusSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restItemStatusMockMvc;

    private ItemStatus itemStatus;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ItemStatusResource itemStatusResource = new ItemStatusResource();
        ReflectionTestUtils.setField(itemStatusResource, "itemStatusSearchRepository", itemStatusSearchRepository);
        ReflectionTestUtils.setField(itemStatusResource, "itemStatusRepository", itemStatusRepository);
        this.restItemStatusMockMvc = MockMvcBuilders.standaloneSetup(itemStatusResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        itemStatus = new ItemStatus();
        itemStatus.setTag(DEFAULT_TAG);
        itemStatus.setCaption(DEFAULT_CAPTION);
    }

    @Test
    @Transactional
    public void createItemStatus() throws Exception {
        int databaseSizeBeforeCreate = itemStatusRepository.findAll().size();

        // Create the ItemStatus

        restItemStatusMockMvc.perform(post("/api/itemStatuss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(itemStatus)))
                .andExpect(status().isCreated());

        // Validate the ItemStatus in the database
        List<ItemStatus> itemStatuss = itemStatusRepository.findAll();
        assertThat(itemStatuss).hasSize(databaseSizeBeforeCreate + 1);
        ItemStatus testItemStatus = itemStatuss.get(itemStatuss.size() - 1);
        assertThat(testItemStatus.getTag()).isEqualTo(DEFAULT_TAG);
        assertThat(testItemStatus.getCaption()).isEqualTo(DEFAULT_CAPTION);
    }

    @Test
    @Transactional
    public void checkTagIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemStatusRepository.findAll().size();
        // set the field null
        itemStatus.setTag(null);

        // Create the ItemStatus, which fails.

        restItemStatusMockMvc.perform(post("/api/itemStatuss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(itemStatus)))
                .andExpect(status().isBadRequest());

        List<ItemStatus> itemStatuss = itemStatusRepository.findAll();
        assertThat(itemStatuss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCaptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemStatusRepository.findAll().size();
        // set the field null
        itemStatus.setCaption(null);

        // Create the ItemStatus, which fails.

        restItemStatusMockMvc.perform(post("/api/itemStatuss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(itemStatus)))
                .andExpect(status().isBadRequest());

        List<ItemStatus> itemStatuss = itemStatusRepository.findAll();
        assertThat(itemStatuss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllItemStatuss() throws Exception {
        // Initialize the database
        itemStatusRepository.saveAndFlush(itemStatus);

        // Get all the itemStatuss
        restItemStatusMockMvc.perform(get("/api/itemStatuss?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(itemStatus.getId().intValue())))
                .andExpect(jsonPath("$.[*].tag").value(hasItem(DEFAULT_TAG.toString())))
                .andExpect(jsonPath("$.[*].caption").value(hasItem(DEFAULT_CAPTION.toString())));
    }

    @Test
    @Transactional
    public void getItemStatus() throws Exception {
        // Initialize the database
        itemStatusRepository.saveAndFlush(itemStatus);

        // Get the itemStatus
        restItemStatusMockMvc.perform(get("/api/itemStatuss/{id}", itemStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(itemStatus.getId().intValue()))
            .andExpect(jsonPath("$.tag").value(DEFAULT_TAG.toString()))
            .andExpect(jsonPath("$.caption").value(DEFAULT_CAPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingItemStatus() throws Exception {
        // Get the itemStatus
        restItemStatusMockMvc.perform(get("/api/itemStatuss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateItemStatus() throws Exception {
        // Initialize the database
        itemStatusRepository.saveAndFlush(itemStatus);

		int databaseSizeBeforeUpdate = itemStatusRepository.findAll().size();

        // Update the itemStatus
        itemStatus.setTag(UPDATED_TAG);
        itemStatus.setCaption(UPDATED_CAPTION);

        restItemStatusMockMvc.perform(put("/api/itemStatuss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(itemStatus)))
                .andExpect(status().isOk());

        // Validate the ItemStatus in the database
        List<ItemStatus> itemStatuss = itemStatusRepository.findAll();
        assertThat(itemStatuss).hasSize(databaseSizeBeforeUpdate);
        ItemStatus testItemStatus = itemStatuss.get(itemStatuss.size() - 1);
        assertThat(testItemStatus.getTag()).isEqualTo(UPDATED_TAG);
        assertThat(testItemStatus.getCaption()).isEqualTo(UPDATED_CAPTION);
    }

    @Test
    @Transactional
    public void deleteItemStatus() throws Exception {
        // Initialize the database
        itemStatusRepository.saveAndFlush(itemStatus);

		int databaseSizeBeforeDelete = itemStatusRepository.findAll().size();

        // Get the itemStatus
        restItemStatusMockMvc.perform(delete("/api/itemStatuss/{id}", itemStatus.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ItemStatus> itemStatuss = itemStatusRepository.findAll();
        assertThat(itemStatuss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
