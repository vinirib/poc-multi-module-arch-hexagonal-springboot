package io.github.wesleyosantos91.core.service;

import io.github.wesleyosantos91.core.domain.PersonDomain;
import io.github.wesleyosantos91.ports.api.PersonServicePort;
import io.github.wesleyosantos91.ports.spi.PersonDatabasePort;
import java.util.List;
import java.util.Optional;

public class PersonServicePortImpl implements PersonServicePort {

    private final PersonDatabasePort personDatabasePort;

    public PersonServicePortImpl(PersonDatabasePort personDatabasePort) {
        this.personDatabasePort = personDatabasePort;
    }

    @Override
    public List<PersonDomain> find() {
        return personDatabasePort.find();
    }

    @Override
    public Optional<PersonDomain> findById(Long id) {
        return personDatabasePort.findById(id);
    }

    @Override
    public PersonDomain exist(Long id) {
        return personDatabasePort.exist(id);
    }

    @Override
    public PersonDomain create(PersonDomain personDomain) {
        return personDatabasePort.create(personDomain);
    }

    @Override
    public PersonDomain update(Long id, PersonDomain personDomain) {
        return personDatabasePort.update(id, personDomain);
    }

    @Override
    public void delete(Long id) {
        personDatabasePort.delete(id);
    }
}
