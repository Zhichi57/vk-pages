package main.api.Data;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class GroupConverter {

    private final ModelMapper modelMapper;

    public GroupConverter() {
        this.modelMapper = new ModelMapper();
    }

    public GroupDto convertToResponse(Group group){
        return modelMapper.map(group, GroupDto.class);
    }
}
