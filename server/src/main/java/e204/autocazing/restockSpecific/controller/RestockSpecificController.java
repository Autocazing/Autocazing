package e204.autocazing.restockSpecific.controller;

import e204.autocazing.restockSpecific.dto.PostRestockSpecificDto;
import e204.autocazing.restockSpecific.dto.RestockSpecificDto;
import e204.autocazing.restockSpecific.dto.UpdateRestockSpecificDto;
import e204.autocazing.restockSpecific.service.RestockSpecificService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/restock-specifics")
public class RestockSpecificController {
    @Autowired
    private RestockSpecificService service;

    @PostMapping("")
    public ResponseEntity createRestockOrderSpecific(@RequestBody PostRestockSpecificDto postRestockSpecificDto) {
        service.createRestockOrderSpecific(postRestockSpecificDto);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<RestockSpecificDto>> getAllRestockOrderSpecifics() {
        List<RestockSpecificDto> RestockSpecifics = service.findAllRestockOrderSpecifics();
        return ResponseEntity.ok(RestockSpecifics);
    }

    @GetMapping("/{restockOrderSpecificId}")
    public ResponseEntity<RestockSpecificDto> getRestockOrderSpecificById(@PathVariable(name = "restockOrderSpecificId") Integer restockOrderSpecificId) {
        RestockSpecificDto specificDto = service.findRestockOrderSpecificById(restockOrderSpecificId);
        return ResponseEntity.ok(specificDto);
    }

    @PutMapping("/{restockOrderSpecificId}")
    public ResponseEntity<RestockSpecificDto> updateRestockOrderSpecific(@PathVariable(name = "restockOrderSpecificId") Integer restockOrderSpecificId,
                                                                         @RequestBody UpdateRestockSpecificDto updateRestockSpecificDto) {
        RestockSpecificDto restockSpecific = service.updateRestockOrderSpecific(restockOrderSpecificId, updateRestockSpecificDto);
        return ResponseEntity.ok(restockSpecific);
    }

    @DeleteMapping("/{restockOrderSpecificId}")
    public ResponseEntity<Void> deleteRestockOrderSpecific(@PathVariable(name = "restockOrderSpecificId") Integer restockOrderSpecificId) {
        service.deleteRestockOrderSpecific(restockOrderSpecificId);
        return ResponseEntity.ok().build();
    }
}
