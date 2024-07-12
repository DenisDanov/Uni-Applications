package bg.duosoft.uniapplicationdemo.repositories.custom.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BaseRepositoryCustomImpl {

    @PersistenceContext
    protected EntityManager em;

}