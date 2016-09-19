package com.byporti.test.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.byporti.dao.PersonDAO;
import com.byporti.entity.Person;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:com/byporti/test/config/data-context.xml",
		"classpath:com/byporti/config/dao-context.xml" })
@ActiveProfiles("testProfile")
public class PersonDaoTest {

	private JdbcTemplate jdbc;

	@Autowired
	private DataSource datasource;

	@Autowired
	private PersonDAO personDao;

	// Test Persons
	private Person person1 = new Person("Cihan", "Güllü", 22);
	private Person person2 = new Person("Yakup", "Yalçın", 22);
	private Person person3 = new Person("Umut", "Yüksel", 23);
	private Person person4 = new Person("Talha", "Güllü", 9);

	@Before
	public void init() {
		jdbc = new JdbcTemplate(datasource);

		// Her test aşamasından önce veritabanının temiz olduğundan emin olmak
		// için.
		jdbc.execute("delete from person");
	}

	@Test
	public void testInsertAndGetAllPersons() {

		personDao.insert(person1);

		List<Person> persons1 = personDao.getAllPersons();
		assertEquals("Toplamda 1 Kişi kayıtlı bulunmalı.", 1, persons1.size());
		assertThat("Kaydedilen kişinin ismi 'Cihan' olmalı.", person1.getName(), is("Cihan"));

		personDao.insert(person2);
		personDao.insert(person3);
		personDao.insert(person4);

		List<Person> persons2 = personDao.getAllPersons();
		assertEquals("Toplamda 4 Kişi kayıtlı bulunmalı.", 4, persons2.size());

	}

	@Test
	public void testGetPersonById() {

		personDao.insert(person4);

		Person alınanKisi = personDao.getPersonById(person4.getId());
		assertEquals("Kayıtlı kişi ile alınan kişi eşleşmeli.", person4, alınanKisi);

		Person alınanKisi2 = personDao.getPersonById(person2.getId());
		assertNull("Böyle bir kişi olmamalı.Çünkü ekleme yapılmadı", alınanKisi2);

	}

	@Test
	public void testGetPersonByName() {

		personDao.insert(person3);

		Person alınanKisi = personDao.getPersonByName(person3.getName());
		assertEquals("Kayıtlı kişi ile alınan kişi eşleşmeli.", person3, alınanKisi);

		Person alınanKisi2 = personDao.getPersonByName("asdqwe123");
		assertNull("'asdqwe123' adında birisi olmamalı.Çünkü ekleme yapılmadı", alınanKisi2);

	}

	@Test
	public void testUpdate() {

		personDao.insert(person2);

		person2.setSurname("UPDATE EDİLDİ.");
		personDao.update(person2);

		Person alınanKisi = personDao.getPersonById(person2.getId());
		assertEquals("Update edilmiş kişi ile alınan kişi eşleşmeli.", person2, alınanKisi);

	}

	@Test
	public void testDelete() {

		personDao.insert(person1);

		Person alinanKisi = personDao.getPersonById(person1.getId());
		assertNotNull(alinanKisi.getId() + " değerine sahip kayıtlı bir kişi olmalı.", alinanKisi);

		personDao.delete(person1);

		Person alinanKisi2 = personDao.getPersonById(person1.getId());
		assertNull(alinanKisi.getId() + " değerine sahip kayıtlı kullanıcı olmamalı.(Silinmiş olmalı.)", alinanKisi2);

	}

	@Test
	public void testDelete2() {
		personDao.insert(person4);

		Person alinanKisi = personDao.getPersonById(person4.getId());
		assertNotNull(alinanKisi.getId() + " değerine sahip kayıtlı bir kişi olmalı.", alinanKisi);

		personDao.delete(person4);

		Person alinanKisi2 = personDao.getPersonById(person4.getId());
		assertNull(alinanKisi.getId() + " değerine sahip kayıtlı kullanıcı olmamalı.(Silinmiş olmalı.)", alinanKisi2);

	}
}
