package br.edu.christus.backend.service;

import br.edu.christus.backend.domain.dto.UserDTO;
import br.edu.christus.backend.domain.dto.UserLowDTO;
import br.edu.christus.backend.domain.model.User;
import br.edu.christus.backend.repository.UserRepository;
import br.edu.christus.backend.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public UserLowDTO create(UserDTO dto){
        if(dto.getName().length() > 100){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "O limite de caracteres do nome é 100");
        }

        if(repository.existsByEmail(dto.getEmail())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "O e-mail já está registrado !");
        }


        var entity = MapperUtil.parseObject(dto, User.class);
        return MapperUtil.parseObject(
                repository.save(entity), UserLowDTO.class);
    }


    public User update(User entity){
        if(entity.getId() == null){
            return null;
        }
        return repository.save(entity);
    }

    public List<UserLowDTO> findUsers(){
        return MapperUtil.parseListObjects(
                repository.findAll(), UserLowDTO.class);
    }

    public void delete(Long id){
        repository.deleteById(id);
    }





}
