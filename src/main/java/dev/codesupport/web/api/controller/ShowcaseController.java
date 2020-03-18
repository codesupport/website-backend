package dev.codesupport.web.api.controller;

import dev.codesupport.web.domain.Showcase;
import dev.codesupport.web.domain.VoidMethodResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * Defines endpoints and validations for the associated API Contract for the {@link Showcase} resource.
 */
@RestController
@RequestMapping("api/showcase/v1")
@Api(value = "Showcase", description = "REST API for Showcases", tags = {"Showcase"})
@Validated
public interface ShowcaseController {

    @ApiOperation("Get all Showcases")
    @GetMapping("/showcases")
    List<Showcase> findAllShowcases();

    @ApiOperation("Get Showcase by id")
    @GetMapping("/showcases/{id}")
    Showcase getShowcaseById(@PathVariable Long id);

    @ApiOperation("Create a Showcase")
    @PostMapping("/showcases")
    Showcase createShowcase(@RequestBody @Valid Showcase showcase);

    @ApiOperation("Update a Showcase")
    @PutMapping("/showcases")
    Showcase updateShowcase(@RequestBody @Valid Showcase showcase);

    @ApiOperation("Delete a Showcase")
    @DeleteMapping("/showcases")
    VoidMethodResponse deleteShowcase(@RequestBody @Valid Showcase showcase);

}
