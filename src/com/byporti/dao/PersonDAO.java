package com.byporti.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.byporti.entity.Person;

@Transactional
@Repository("personDAO")
public class PersonDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public Session session() {
		return sessionFactory.getCurrentSession();
	}

	public void insert(Person person) {
		session().save(person);
	}

	public Person getPersonById(int id) {
		Criteria crit = session().createCriteria(Person.class);
		crit.add(Restrictions.idEq(id));
		return (Person) crit.uniqueResult();
	}

	public Person getPersonByName(String name) {
		Criteria crit = session().createCriteria(Person.class);
		crit.add(Restrictions.eq("name", name));
		return (Person) crit.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<Person> getAllPersons() {
		return session().createQuery("from Person").list();
	}

	public void update(Person person) {
		session().update(person);
	}

	public void delete(Person person) {
		session().delete(person);
	}

	public boolean delete(int id) {
		Query query = session().createQuery("delete from Person where id=:id");
		query.setLong("id", id);

		return query.executeUpdate() == 1;
	}

}
