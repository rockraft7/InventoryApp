package com.faizalsidek.inventory.web.rest;

import com.faizalsidek.inventory.Application;
import com.faizalsidek.inventory.domain.ItemModel;
import com.faizalsidek.inventory.repository.ItemModelRepository;
import com.faizalsidek.inventory.repository.search.ItemModelSearchRepository;

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
 * Test class for the ItemModelResource REST controller.
 *
 * @see ItemModelResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ItemModelResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private ItemModelRepository itemModelRepository;

    @Inject
    private ItemModelSearchRepository itemModelSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restItemModelMockMvc;

    private ItemModel itemModel;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ItemModelResource itemModelResource = new ItemModelResource();
        ReflectionTestUtils.setField(itemModelResource, "itemModelSearchRepository", itemModelSearchRepository);
        ReflectionTestUtils.setField(itemModelResource, "itemModelRepository", itemModelRepository);
        this.restItemModelMockMvc = MockMvcBuilders.standaloneSetup(itemModelResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        itemModel = new ItemModel();
        itemModel.setName(DEFAULT_NAME);
        itemModel.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createItemModel() throws Exception {
        int databaseSizeBeforeCreate = itemModelRepository.findAll().size();

        // Create the ItemModel

        restItemModelMockMvc.perform(post("/api/itemModels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(itemModel)))
                .andExpect(status().isCreated());

        // Validate the ItemModel in the database
        List<ItemModel> itemModels = itemModelRepository.findAll();
        assertThat(itemModels).hasSize(databaseSizeBeforeCreate + 1);
        ItemModel testItemModel = itemModels.get(itemModels.size() - 1);
        assertThat(testItemModel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testItemModel.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemModelRepository.findAll().size();
        // set the field null
        itemModel.setName(null);

        // Create the ItemModel, which fails.

        restItemModelMockMvc.perform(post("/api/itemModels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(itemModel)))
                .andExpect(status().isBadRequest());

        List<ItemModel> itemModels = itemModelRepository.findAll();
        assertThat(itemModels).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllItemModels() throws Exception {
        // Initialize the database
        itemModelRepository.saveAndFlush(itemModel);

        // Get all the itemModels
        restItemModelMockMvc.perform(get("/api/itemModels?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(itemModel.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getItemModel() throws Exception {
        // Initialize the database
        itemModelRepository.saveAndFlush(itemModel);

        // Get the itemModel
        restItemModelMockMvc.perform(get("/api/itemModels/{id}", itemModel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(itemModel.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingItemModel() throws Exception {
        // Get the itemModel
        restItemModelMockMvc.perform(get("/api/itemModels/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateItemModel() throws Exception {
        // Initialize the database
        itemModelRepository.saveAndFlush(itemModel);

		int databaseSizeBeforeUpdate = itemModelRepository.findAll().size();

        // Update the itemModel
        itemModel.setName(UPDATED_NAME);
        itemModel.setDescription(UPDATED_DESCRIPTION);

        restItemModelMockMvc.perform(put("/api/itemModels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(itemModel)))
                .andExpect(status().isOk());

        // Validate the ItemModel in the database
        List<ItemModel> itemModels = itemModelRepository.findAll();
        assertThat(itemModels).hasSize(databaseSizeBeforeUpdate);
        ItemModel testItemModel = itemModels.get(itemModels.size() - 1);
        assertThat(testItemModel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testItemModel.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteItemModel() throws Exception {
        // Initialize the database
        itemModelRepository.saveAndFlush(itemModel);

		int databaseSizeBeforeDelete = itemModelRepository.findAll().size();

        // Get the itemModel
        restItemModelMockMvc.perform(delete("/api/itemModels/{id}", itemModel.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ItemModel> itemModels = itemModelRepository.findAll();
        assertThat(itemModels).hasSize(databaseSizeBeforeDelete - 1);
    }
}
