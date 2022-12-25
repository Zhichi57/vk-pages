package main.api.Service;

import main.api.Data.Query;
import main.api.Repository.QueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QueryServiceImpl implements QueryService {

    @Autowired
    private QueryRepository queryRepository;

    @Override
    public Integer addQuery(Query query) {
        Query save_query = queryRepository.save(query);
        return save_query.getId();

    }
}
