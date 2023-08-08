package com.sh.domain.user.repository;

import com.sh.domain.user.domain.BlackListToken;
import org.springframework.data.repository.CrudRepository;

public interface BlackListTokenRedisRepository extends CrudRepository<BlackListToken, String> {

}
