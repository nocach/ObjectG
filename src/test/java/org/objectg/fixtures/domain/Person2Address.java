package org.objectg.fixtures.domain;

import javax.persistence.*;
import java.util.Set;

/**
 * User: __nocach
 * Date: 12.10.12
 */
@Entity
public class Person2Address {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Person person;
    @OneToOne
    private Person owner;
    @OneToMany
    private Set<Person> dependentPersons;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public Set<Person> getDependentPersons() {
        return dependentPersons;
    }

    public void setDependentPersons(Set<Person> dependentPersons) {
        this.dependentPersons = dependentPersons;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
