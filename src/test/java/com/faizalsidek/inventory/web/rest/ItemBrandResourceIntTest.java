package com.faizalsidek.inventory.web.rest;

import com.faizalsidek.inventory.Application;
import com.faizalsidek.inventory.domain.ItemBrand;
import com.faizalsidek.inventory.repository.ItemBrandRepository;
import com.faizalsidek.inventory.repository.search.ItemBrandSearchRepository;

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
 * Test class for the ItemBrandResource REST controller.
 *
 * @see ItemBrandResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ItemBrandResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private ItemBrandRepository itemBrandRepository;

    @Inject
    private ItemBrandSearchRepository itemBrandSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restItemBrandMockMvc;

    private ItemBrand itemBrand;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ItemBrandResource itemBrandResource = new ItemBrandResource();
        ReflectionTestUtils.setField(itemBrandResource, "itemBrandSearchRepository", itemBrandSearchRepository);
        ReflectionTestUtils.setField(itemBrandResource, "itemBrandRepository", itemBrandRepository);
        this.restItemBrandMockMvc = MockMvcBuilders.standaloneSetup(itemBrandResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        itemBrand = new ItemBrand();
        itemBrand.setName(DEFAULT_NAME);
        itemBrand.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createItemBrand() throws Exception {
        int databaseSizeBeforeCreate = itemBrandRepository.findAll().size();

        // Create the ItemBrand

        restItemBrandMockMvc.perform(post("/api/itemBrands")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(itemBrand)))
                .andExpect(status().isCreated());

        // Validate the ItemBrand in the database
        List<ItemBrand> itemBrands = itemBrandRepository.findAll();
        assertThat(itemBrands).hasSize(databaseSizeBeforeCreate + 1);
        ItemBrand testItemBrand = itemBrands.get(itemBrands.size() - 1);
        assertThat(testItemBrand.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testItemBrand.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemBrandRepository.findAll().size();
        // set the field null
        itemBrand.setName(null);

        // Create the ItemBrand, which fails.

        restItemBrandMockMvc.perform(post("/api/itemBrands")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(itemBrand)))
                .andExpect(status().isBadRequest());

        List<ItemBrand> itemBrands = itemBrandRepository.findAll();
        assertThat(itemBrands).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllItemBrands() throws Exception {
        // Initialize the database
        itemBrandRepository.saveAndFlush(itemBrand);

        // Get all the itemBrands
        restItemBrandMockMvc.perform(get("/api/itemBrands?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(itemBrand.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getItemBrand() throws Exception {
        // Initialize the database
        itemBrandRepository.saveAndFlush(itemBrand);

        // Get the itemBrand
        restItemBrandMockMvc.perform(get("/api/itemBrands/{id}", itemBrand.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(itemBrand.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingItemBrand() throws Exception {
        // Get the itemBrand
        restItemBrandMockMvc.perform(get("/api/itemBrands/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateItemBrand() throws Exception {
        // Initialize the database
        itemBrandRepository.saveAndFlush(itemBrand);

		int databaseSizeBeforeUpdate = itemBrandRepository.findAll().size();

        // Update the itemBrand
        itemBrand.setName(UPDATED_NAME);
        itemBrand.setDescription(UPDATED_DESCRIPTION);

        restItemBrandMockMvc.perform(put("/api/itemBrands")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(itemBrand)))
                .andExpect(status().isOk());

        // Validate the ItemBrand in the database
        List<ItemBrand> itemBrands = itemBrandRepository.findAll();
        assertThat(itemBrands).hasSize(databaseSizeBeforeUpdate);
        ItemBrand testItemBrand = itemBrands.get(itemBrands.size() - 1);
        assertThat(testItemBrand.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testItemBrand.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteItemBrand() throws Exception {
        // Initialize the database
        itemBrandRepository.saveAndFlush(itemBrand);

		int databaseSizeBeforeDelete = itemBrandRepository.findAll().size();

        // Get the itemBrand
        restItemBrandMockMvc.perform(delete("/api/itemBrands/{id}", itemBrand.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ItemBrand> itemBrands = itemBrandRepository.findAll();
        assertThat(itemBrands).hasSize(databaseSizeBeforeDelete - 1);
    }
}
