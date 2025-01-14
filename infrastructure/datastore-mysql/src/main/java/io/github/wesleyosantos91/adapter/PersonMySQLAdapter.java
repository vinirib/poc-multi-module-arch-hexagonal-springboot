package io.github.wesleyosantos91.adapter;

import static io.github.wesleyosantos91.mapper.PersonMapper.INSTANCE;

import io.github.wesleyosantos91.core.domain.PersonDomain;
import io.github.wesleyosantos91.core.exception.ResourceNotFoundException;
import io.github.wesleyosantos91.ports.spi.PersonDatabasePort;
import io.github.wesleyosantos91.repository.PersonRepository;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Primary
public class PersonMySQLAdapter implements PersonDatabasePort {

    private final PersonRepository repository;

    public PersonMySQLAdapter(PersonRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<PersonDomain> find() {
        return INSTANCE.toListDomain(repository.findAll());
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<PersonDomain> findById(Long id) {
        return Optional.of(exist(id));
    }

    @Transactional
    @Override
    public PersonDomain exist(Long id) {
        var personEntity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageFormat.format("Not found regitstry with code {0}", id)));
        return INSTANCE.toDomain(personEntity);
    }

    @Transactional
    @Override
    public PersonDomain create(PersonDomain personDomain) {
        return INSTANCE.toDomain(repository.save(INSTANCE.toEntity(personDomain)));
    }

    @Transactional
    @Override
    public PersonDomain update(Long id, PersonDomain personDomain) {
        var personSaved = exist(id);
        BeanUtils.copyProperties(personDomain, personSaved, "id");
        return INSTANCE.toDomain(repository.save(INSTANCE.toEntity(personSaved)));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        var personSaved = exist(id);
        repository.delete(INSTANCE.toEntity(personSaved));
    }
}
