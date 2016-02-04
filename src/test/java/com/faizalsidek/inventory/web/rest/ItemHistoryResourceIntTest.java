package com.faizalsidek.inventory.web.rest;

import com.faizalsidek.inventory.Application;
import com.faizalsidek.inventory.domain.ItemHistory;
import com.faizalsidek.inventory.repository.ItemHistoryRepository;
import com.faizalsidek.inventory.repository.search.ItemHistorySearchRepository;

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
 * Test class for the ItemHistoryResource REST controller.
 *
 * @see ItemHistoryResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ItemHistoryResourceIntTest {


    @Inject
    private ItemHistoryRepository itemHistoryRepository;

    @Inject
    private ItemHistorySearchRepository itemHistorySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restItemHistoryMockMvc;

    private ItemHistory itemHistory;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ItemHistoryResource itemHistoryResource = new ItemHistoryResource();
        ReflectionTestUtils.setField(itemHistoryResource, "itemHistorySearchRepository", itemHistorySearchRepository);
        ReflectionTestUtils.setField(itemHistoryResource, "itemHistoryRepository", itemHistoryRepository);
        this.restItemHistoryMockMvc = MockMvcBuilders.standaloneSetup(itemHistoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        itemHistory = new ItemHistory();
    }

    @Test
    @Transactional
    public void createItemHistory() throws Exception {
        int databaseSizeBeforeCreate = itemHistoryRepository.findAll().size();

        // Create the ItemHistory

        restItemHistoryMockMvc.perform(post("/api/itemHistorys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(itemHistory)))
                .andExpect(status().isCreated());

        // Validate the ItemHistory in the database
        List<ItemHistory> itemHistorys = itemHistoryRepository.findAll();
        assertThat(itemHistorys).hasSize(databaseSizeBeforeCreate + 1);
        ItemHistory testItemHistory = itemHistorys.get(itemHistorys.size() - 1);
    }

    @Test
    @Transactional
    public void getAllItemHistorys() throws Exception {
        // Initialize the database
        itemHistoryRepository.saveAndFlush(itemHistory);

        // Get all the itemHistorys
        restItemHistoryMockMvc.perform(get("/api/itemHistorys?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(itemHistory.getId().intValue())));
    }

    @Test
    @Transactional
    public void getItemHistory() throws Exception {
        // Initialize the database
        itemHistoryRepository.saveAndFlush(itemHistory);

        // Get the itemHistory
        restItemHistoryMockMvc.perform(get("/api/itemHistorys/{id}", itemHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(itemHistory.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingItemHistory() throws Exception {
        // Get the itemHistory
        restItemHistoryMockMvc.perform(get("/api/itemHistorys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateItemHistory() throws Exception {
        // Initialize the database
        itemHistoryRepository.saveAndFlush(itemHistory);

		int databaseSizeBeforeUpdate = itemHistoryRepository.findAll().size();

        // Update the itemHistory

        restItemHistoryMockMvc.perform(put("/api/itemHistorys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(itemHistory)))
                .andExpect(status().isOk());

        // Validate the ItemHistory in the database
        List<ItemHistory> itemHistorys = itemHistoryRepository.findAll();
        assertThat(itemHistorys).hasSize(databaseSizeBeforeUpdate);
        ItemHistory testItemHistory = itemHistorys.get(itemHistorys.size() - 1);
    }

    @Test
    @Transactional
    public void deleteItemHistory() throws Exception {
        // Initialize the database
        itemHistoryRepository.saveAndFlush(itemHistory);

		int databaseSizeBeforeDelete = itemHistoryRepository.findAll().size();

        // Get the itemHistory
        restItemHistoryMockMvc.perform(delete("/api/itemHistorys/{id}", itemHistory.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ItemHistory> itemHistorys = itemHistoryRepository.findAll();
        assertThat(itemHistorys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
