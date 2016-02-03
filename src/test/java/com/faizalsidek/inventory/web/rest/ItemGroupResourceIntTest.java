package com.faizalsidek.inventory.web.rest;

import com.faizalsidek.inventory.Application;
import com.faizalsidek.inventory.domain.ItemGroup;
import com.faizalsidek.inventory.repository.ItemGroupRepository;
import com.faizalsidek.inventory.repository.search.ItemGroupSearchRepository;

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
 * Test class for the ItemGroupResource REST controller.
 *
 * @see ItemGroupResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ItemGroupResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_TAG = "AAAAA";
    private static final String UPDATED_TAG = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private ItemGroupRepository itemGroupRepository;

    @Inject
    private ItemGroupSearchRepository itemGroupSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restItemGroupMockMvc;

    private ItemGroup itemGroup;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ItemGroupResource itemGroupResource = new ItemGroupResource();
        ReflectionTestUtils.setField(itemGroupResource, "itemGroupSearchRepository", itemGroupSearchRepository);
        ReflectionTestUtils.setField(itemGroupResource, "itemGroupRepository", itemGroupRepository);
        this.restItemGroupMockMvc = MockMvcBuilders.standaloneSetup(itemGroupResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        itemGroup = new ItemGroup();
        itemGroup.setName(DEFAULT_NAME);
        itemGroup.setTag(DEFAULT_TAG);
        itemGroup.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createItemGroup() throws Exception {
        int databaseSizeBeforeCreate = itemGroupRepository.findAll().size();

        // Create the ItemGroup

        restItemGroupMockMvc.perform(post("/api/itemGroups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(itemGroup)))
                .andExpect(status().isCreated());

        // Validate the ItemGroup in the database
        List<ItemGroup> itemGroups = itemGroupRepository.findAll();
        assertThat(itemGroups).hasSize(databaseSizeBeforeCreate + 1);
        ItemGroup testItemGroup = itemGroups.get(itemGroups.size() - 1);
        assertThat(testItemGroup.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testItemGroup.getTag()).isEqualTo(DEFAULT_TAG);
        assertThat(testItemGroup.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemGroupRepository.findAll().size();
        // set the field null
        itemGroup.setName(null);

        // Create the ItemGroup, which fails.

        restItemGroupMockMvc.perform(post("/api/itemGroups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(itemGroup)))
                .andExpect(status().isBadRequest());

        List<ItemGroup> itemGroups = itemGroupRepository.findAll();
        assertThat(itemGroups).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTagIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemGroupRepository.findAll().size();
        // set the field null
        itemGroup.setTag(null);

        // Create the ItemGroup, which fails.

        restItemGroupMockMvc.perform(post("/api/itemGroups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(itemGroup)))
                .andExpect(status().isBadRequest());

        List<ItemGroup> itemGroups = itemGroupRepository.findAll();
        assertThat(itemGroups).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllItemGroups() throws Exception {
        // Initialize the database
        itemGroupRepository.saveAndFlush(itemGroup);

        // Get all the itemGroups
        restItemGroupMockMvc.perform(get("/api/itemGroups?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(itemGroup.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].tag").value(hasItem(DEFAULT_TAG.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getItemGroup() throws Exception {
        // Initialize the database
        itemGroupRepository.saveAndFlush(itemGroup);

        // Get the itemGroup
        restItemGroupMockMvc.perform(get("/api/itemGroups/{id}", itemGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(itemGroup.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.tag").value(DEFAULT_TAG.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingItemGroup() throws Exception {
        // Get the itemGroup
        restItemGroupMockMvc.perform(get("/api/itemGroups/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateItemGroup() throws Exception {
        // Initialize the database
        itemGroupRepository.saveAndFlush(itemGroup);

		int databaseSizeBeforeUpdate = itemGroupRepository.findAll().size();

        // Update the itemGroup
        itemGroup.setName(UPDATED_NAME);
        itemGroup.setTag(UPDATED_TAG);
        itemGroup.setDescription(UPDATED_DESCRIPTION);

        restItemGroupMockMvc.perform(put("/api/itemGroups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(itemGroup)))
                .andExpect(status().isOk());

        // Validate the ItemGroup in the database
        List<ItemGroup> itemGroups = itemGroupRepository.findAll();
        assertThat(itemGroups).hasSize(databaseSizeBeforeUpdate);
        ItemGroup testItemGroup = itemGroups.get(itemGroups.size() - 1);
        assertThat(testItemGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testItemGroup.getTag()).isEqualTo(UPDATED_TAG);
        assertThat(testItemGroup.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteItemGroup() throws Exception {
        // Initialize the database
        itemGroupRepository.saveAndFlush(itemGroup);

		int databaseSizeBeforeDelete = itemGroupRepository.findAll().size();

        // Get the itemGroup
        restItemGroupMockMvc.perform(delete("/api/itemGroups/{id}", itemGroup.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ItemGroup> itemGroups = itemGroupRepository.findAll();
        assertThat(itemGroups).hasSize(databaseSizeBeforeDelete - 1);
    }
}
