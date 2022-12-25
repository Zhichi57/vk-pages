package main.api.Service;

import main.api.Data.Group;
import main.api.Repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService{

    @Autowired
    private GroupRepository groupRepository;

    @Override
    public boolean saveGroup(Group group) {
        groupRepository.save(group);
        return true;
    }

    @Override
    public List<Group> listGroups(int user_id, int page, int size) {
        Pageable firstPageWithTwoElements = PageRequest.of(page, size);
        return groupRepository.findAllByUserid(user_id, firstPageWithTwoElements);
    }
}
