package com.kinteg.frogrammer.controller.tag;

import com.kinteg.frogrammer.dto.tag.CreatedTagDto;
import com.kinteg.frogrammer.dto.tag.SimpleTagDto;
import com.kinteg.frogrammer.service.tag.AdminTagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("api/tag/admin")
@Slf4j
@Validated
public class AdminTagController {

    private final AdminTagService adminTagService;

    public AdminTagController(@Qualifier("adminTagServiceImpl") AdminTagService adminTagService) {
        this.adminTagService = adminTagService;
    }

    @PostMapping(value = "/create", produces = "application/json")
    public ResponseEntity<SimpleTagDto> createTag(@Valid @RequestBody CreatedTagDto tag) {
        SimpleTagDto simpleTagDto = SimpleTagDto.toSimpleTag(adminTagService.createTag(tag));
        return ResponseEntity.ok(simpleTagDto);
    }

    @DeleteMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity<?> deleteTag(@NotNull @Min(1) @PathVariable Long id) {
        adminTagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }

}
