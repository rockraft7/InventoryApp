package com.faizalsidek.inventory.web.rest;

import com.faizalsidek.inventory.Application;
import com.faizalsidek.inventory.domain.StockAcquisition;
import com.faizalsidek.inventory.repository.StockAcquisitionRepository;
import com.faizalsidek.inventory.repository.search.StockAcquisitionSearchRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the StockAcquisitionResource REST controller.
 *
 * @see StockAcquisitionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class StockAcquisitionResourceIntTest {

    private static final String DEFAULT_INVOICE_ID = "AAAAA";
    private static final String UPDATED_INVOICE_ID = "BBBBB";

    private static final LocalDate DEFAULT_DATE_ACQUIRE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_ACQUIRE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_REMARKS = "AAAAA";
    private static final String UPDATED_REMARKS = "BBBBB";

    @Inject
    private StockAcquisitionRepository stockAcquisitionRepository;

    @Inject
    private StockAcquisitionSearchRepository stockAcquisitionSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restStockAcquisitionMockMvc;

    private StockAcquisition stockAcquisition;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StockAcquisitionResource stockAcquisitionResource = new StockAcquisitionResource();
        ReflectionTestUtils.setField(stockAcquisitionResource, "stockAcquisitionSearchRepository", stockAcquisitionSearchRepository);
        ReflectionTestUtils.setField(stockAcquisitionResource, "stockAcquisitionRepository", stockAcquisitionRepository);
        this.restStockAcquisitionMockMvc = MockMvcBuilders.standaloneSetup(stockAcquisitionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        stockAcquisition = new StockAcquisition();
        stockAcquisition.setInvoiceId(DEFAULT_INVOICE_ID);
        stockAcquisition.setDateAcquire(DEFAULT_DATE_ACQUIRE);
        stockAcquisition.setRemarks(DEFAULT_REMARKS);
    }

    @Test
    @Transactional
    public void createStockAcquisition() throws Exception {
        int databaseSizeBeforeCreate = stockAcquisitionRepository.findAll().size();

        // Create the StockAcquisition

        restStockAcquisitionMockMvc.perform(post("/api/stockAcquisitions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockAcquisition)))
                .andExpect(status().isCreated());

        // Validate the StockAcquisition in the database
        List<StockAcquisition> stockAcquisitions = stockAcquisitionRepository.findAll();
        assertThat(stockAcquisitions).hasSize(databaseSizeBeforeCreate + 1);
        StockAcquisition testStockAcquisition = stockAcquisitions.get(stockAcquisitions.size() - 1);
        assertThat(testStockAcquisition.getInvoiceId()).isEqualTo(DEFAULT_INVOICE_ID);
        assertThat(testStockAcquisition.getDateAcquire()).isEqualTo(DEFAULT_DATE_ACQUIRE);
        assertThat(testStockAcquisition.getRemarks()).isEqualTo(DEFAULT_REMARKS);
    }

    @Test
    @Transactional
    public void checkDateAcquireIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockAcquisitionRepository.findAll().size();
        // set the field null
        stockAcquisition.setDateAcquire(null);

        // Create the StockAcquisition, which fails.

        restStockAcquisitionMockMvc.perform(post("/api/stockAcquisitions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockAcquisition)))
                .andExpect(status().isBadRequest());

        List<StockAcquisition> stockAcquisitions = stockAcquisitionRepository.findAll();
        assertThat(stockAcquisitions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStockAcquisitions() throws Exception {
        // Initialize the database
        stockAcquisitionRepository.saveAndFlush(stockAcquisition);

        // Get all the stockAcquisitions
        restStockAcquisitionMockMvc.perform(get("/api/stockAcquisitions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(stockAcquisition.getId().intValue())))
                .andExpect(jsonPath("$.[*].invoiceId").value(hasItem(DEFAULT_INVOICE_ID.toString())))
                .andExpect(jsonPath("$.[*].dateAcquire").value(hasItem(DEFAULT_DATE_ACQUIRE.toString())))
                .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS.toString())));
    }

    @Test
    @Transactional
    public void getStockAcquisition() throws Exception {
        // Initialize the database
        stockAcquisitionRepository.saveAndFlush(stockAcquisition);

        // Get the stockAcquisition
        restStockAcquisitionMockMvc.perform(get("/api/stockAcquisitions/{id}", stockAcquisition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(stockAcquisition.getId().intValue()))
            .andExpect(jsonPath("$.invoiceId").value(DEFAULT_INVOICE_ID.toString()))
            .andExpect(jsonPath("$.dateAcquire").value(DEFAULT_DATE_ACQUIRE.toString()))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStockAcquisition() throws Exception {
        // Get the stockAcquisition
        restStockAcquisitionMockMvc.perform(get("/api/stockAcquisitions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockAcquisition() throws Exception {
        // Initialize the database
        stockAcquisitionRepository.saveAndFlush(stockAcquisition);

		int databaseSizeBeforeUpdate = stockAcquisitionRepository.findAll().size();

        // Update the stockAcquisition
        stockAcquisition.setInvoiceId(UPDATED_INVOICE_ID);
        stockAcquisition.setDateAcquire(UPDATED_DATE_ACQUIRE);
        stockAcquisition.setRemarks(UPDATED_REMARKS);

        restStockAcquisitionMockMvc.perform(put("/api/stockAcquisitions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockAcquisition)))
                .andExpect(status().isOk());

        // Validate the StockAcquisition in the database
        List<StockAcquisition> stockAcquisitions = stockAcquisitionRepository.findAll();
        assertThat(stockAcquisitions).hasSize(databaseSizeBeforeUpdate);
        StockAcquisition testStockAcquisition = stockAcquisitions.get(stockAcquisitions.size() - 1);
        assertThat(testStockAcquisition.getInvoiceId()).isEqualTo(UPDATED_INVOICE_ID);
        assertThat(testStockAcquisition.getDateAcquire()).isEqualTo(UPDATED_DATE_ACQUIRE);
        assertThat(testStockAcquisition.getRemarks()).isEqualTo(UPDATED_REMARKS);
    }

    @Test
    @Transactional
    public void deleteStockAcquisition() throws Exception {
        // Initialize the database
        stockAcquisitionRepository.saveAndFlush(stockAcquisition);

		int databaseSizeBeforeDelete = stockAcquisitionRepository.findAll().size();

        // Get the stockAcquisition
        restStockAcquisitionMockMvc.perform(delete("/api/stockAcquisitions/{id}", stockAcquisition.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<StockAcquisition> stockAcquisitions = stockAcquisitionRepository.findAll();
        assertThat(stockAcquisitions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
