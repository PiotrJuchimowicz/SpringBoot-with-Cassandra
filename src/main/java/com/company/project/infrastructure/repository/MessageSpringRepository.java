package com.company.project.infrastructure.repository;

import com.company.project.domain.Message;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageSpringRepository extends CassandraRepository<Message, Long> {

    List<Message> findAllByMagicNumber(Integer magicNumber);

    long countAllByEmail(String email);

    Slice<Message> findAllByEmail(String email, Pageable pageable);
}
