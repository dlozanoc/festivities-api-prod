package com.github.dlozanoc.festapivity.application.festivities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.github.dlozanoc.festapivity.application.domain.Festivity;
import com.github.dlozanoc.festapivity.application.integration.FestivityListResource;
import com.github.dlozanoc.festapivity.application.integration.FestivityResource;
import com.github.dlozanoc.festapivity.configuration.ApplicationConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationConfiguration.class })
@WebAppConfiguration
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class FestivityControllerTest {
	
	@Mock
	private FestivityService festivityService;
	
	@InjectMocks
	@Autowired
	private FestivityController testSubject;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldReturnSomeContentWhenAllFestivitiesRequested() {
		// given
		List<Festivity> festivities = Arrays.asList(
				new Festivity[] {
				    new Festivity(1L, "Fest", ZonedDateTime.now(), ZonedDateTime.now(), "Some place"),
				    new Festivity(2L, "Fest 2", ZonedDateTime.now(), ZonedDateTime.now(), "Another place")
				});
		when(festivityService.getAll()).thenReturn(Optional.of(festivities));
		
		// when
		ResponseEntity<FestivityListResource> response = testSubject.getAll(null, null, null, null);
		
		// then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, response.getBody().getFestivities().size());
	}
	
	@Test
	public void shouldNotReturnContentAsNoFestivitiesWereFoundWhenAllFestivitiesRequested() {
		// given
		when(festivityService.getAll()).thenReturn(Optional.of(Collections.emptyList()));
		
		// when
		ResponseEntity<FestivityListResource> response = testSubject.getAll(null, null, null, null);
		
		// then
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNull(response.getBody());
	}

	@Test
	public void shouldNotReturnContentWhenAllFestivitiesRequestedDueToAnError() {
		// given
		when(festivityService.getAll()).thenReturn(Optional.empty());
		
		// when
		ResponseEntity<FestivityListResource> response = testSubject.getAll(null, null, null, null);
		
		// then
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertNull(response.getBody());
	}
	
	@Test
	public void shouldReturnFestivityWhenRequestedById() {
		// given
		when(festivityService.getById(1L)).thenReturn(Optional.of(new Festivity(1L, "Fest", ZonedDateTime.now(), ZonedDateTime.now(), "Some place")));
		
		// when
		ResponseEntity<FestivityResource> response = testSubject.getById(1L);
		
		// then
		assertNotNull(response.getBody());
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void shouldNotReturnFestivityWhenRequestedByIdBecauseOfInexistence() {
		// given
		when(festivityService.getById(1L)).thenReturn(Optional.empty());
		
		// when
		ResponseEntity<FestivityResource> response = testSubject.getById(1L);
		
		// then
		assertNull(response.getBody());
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
	
	@Test
	public void shouldNotReturnFestivityWhenRequestedByIdBecauseOfError() {
		// given
		when(festivityService.getById(1L)).thenThrow(new NullPointerException());
		
		// when
		ResponseEntity<FestivityResource> response = testSubject.getById(1L);
		
		// then
		assertNull(response.getBody());
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}
	
}
