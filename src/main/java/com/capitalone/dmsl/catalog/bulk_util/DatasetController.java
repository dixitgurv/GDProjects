package com.capitalone.dmsl.catalog.bulk_util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@RestController
@RequestMapping("/api/datasets")
@CrossOrigin(origins = "http://localhost:3000")
public class DatasetController {

    private final DatasetRepository datasetRepository;

    public DatasetController(DatasetRepository datasetRepository) {
        this.datasetRepository = datasetRepository;
    }

    @GetMapping
    public Page<Dataset> getAllDatasets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search) {
        Pageable pageable = PageRequest.of(page, size);

        if (search != null && !search.isEmpty()) {
            return datasetRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(search, search, pageable);
        }
        return datasetRepository.findAll(pageable);
    }

    @PostMapping("/batch")
    public ResponseEntity<Void> createDatasetsInBatches(@RequestBody List<Dataset> datasets) {
        int batchSize = 1000; // Define a batch size
        for (int i = 0; i < datasets.size(); i += batchSize) {
            List<Dataset> batch = datasets.subList(i, Math.min(i + batchSize, datasets.size()));
            datasetRepository.saveAll(batch);
        }
        return ResponseEntity.ok().build();
    }
}