package com.example.resttaskmanager.controller;

import com.example.resttaskmanager.dto.RoleRequest;
import com.example.resttaskmanager.dto.RoleResponse;
import com.example.resttaskmanager.mapper.RoleMapper;
import com.example.resttaskmanager.model.Role;
import com.example.resttaskmanager.service.RoleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@AllArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    private final RoleMapper roleMapper;


    @GetMapping
    public List<RoleResponse> getAllRoles(){
        return roleService.getAll().stream()
                .map(RoleResponse::new)
                .collect(Collectors.toList());
    }


    @GetMapping("/{roleId}")
    public RoleResponse getRoleById(@PathVariable Long roleId){
        Role role = roleService.readById(roleId);
        return new RoleResponse(role);
    }



    @PostMapping
    public RoleResponse createRole (@RequestBody @Validated RoleRequest request,
                                    BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            throw new IllegalArgumentException(bindingResult.getAllErrors().toString());
        }

        return  new RoleResponse(roleService.create(roleMapper.dtoToModel(request)));
    }


    @PutMapping("/{roleId}")
    public RoleResponse updateRoleById(@PathVariable Long roleId,
                                       @RequestBody @Validated RoleRequest request,
                                       BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            throw new IllegalArgumentException(bindingResult.getAllErrors().toString());
        }

        Role convertRole = roleMapper.dtoToModel(request);
        convertRole.setId(roleId);

        return new RoleResponse(roleService.update(convertRole));
    }


    @DeleteMapping("/{roleId}")
    public ResponseEntity<String> DeleteRoleById(@PathVariable Long roleId){

        roleService.delete(roleId);
        return ResponseEntity.ok("Role with id = " + roleId + " has been deleted");
    }

}
