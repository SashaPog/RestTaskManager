package com.example.resttaskmanager.controller;

import com.example.resttaskmanager.dto.StateRequest;
import com.example.resttaskmanager.dto.StateResponse;
import com.example.resttaskmanager.mapper.StateMapper;
import com.example.resttaskmanager.model.State;
import com.example.resttaskmanager.service.StateService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/states")
public class StateController {

    private final StateService stateService;

    private final StateMapper stateMapper;


    @GetMapping
    public List<StateResponse> getAllStates(){
        return stateService.getAll().stream()
                .map(StateResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{stateId}")
    public StateResponse getStateById(@PathVariable Long stateId){
        State state = stateService.readById(stateId);
        return new StateResponse(state);
    }


    @PostMapping
    public StateResponse createState(@RequestBody @Validated StateRequest request,
                                     BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new IllegalArgumentException(bindingResult.getAllErrors().toString());
        }
        return new StateResponse(stateService.create(stateMapper.dtoToModel(request)));
    }

    @PutMapping("/{stateId}")
    public StateResponse updateStateById(@PathVariable Long stateId,
                                         @RequestBody @Validated StateRequest request,
                                         BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            throw new IllegalArgumentException(bindingResult.getAllErrors().toString());
        }

        State convertState = stateMapper.dtoToModel(request);
        convertState.setId(stateId);

        return new StateResponse(stateService.update(convertState));
    }

    @DeleteMapping("/{stateId}")
    public ResponseEntity<String> DeleteStateById(@PathVariable Long stateId){
        stateService.delete(stateId);

        return ResponseEntity.ok("State with id = " + stateId + " has been deleted");
    }
}
