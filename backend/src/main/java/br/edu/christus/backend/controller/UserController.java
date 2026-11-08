package br.edu.christus.backend.controller;

import br.edu.christus.backend.domain.dto.UserDTO;
import br.edu.christus.backend.domain.dto.UserLowDTO;
import br.edu.christus.backend.domain.model.User;
import br.edu.christus.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService service;

    @Operation(summary = "Cadastra os dados de um usuário | role: [ADMIN]", tags = "User")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário cadastro com sucesso"),
            @ApiResponse(responseCode = "409", description = "E-mail já existe!",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(type = "object",
                                    example = """
            {
              "message": "string"
            }
            """)
                    ))
    })
    @PostMapping
    public UserLowDTO create(@RequestBody  UserDTO user){
        return service.create(user);
    }

    @PutMapping
    public User update(@RequestBody User user){
        return service.update(user);
    }

    @Operation(summary = "Retorna os dados de um usuário | role: [ADMIN]", tags = "User")
    @ApiResponse(responseCode = "200", description = "Usuário retornado com sucesso")
    @GetMapping
    public List<UserLowDTO> findAll(){
        return service.findUsers();
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(type = "object",
                                    example = """
            {
              "message": "string"
            }
            """)
                    ))
    })
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id){
        service.delete(id);
    }





}
