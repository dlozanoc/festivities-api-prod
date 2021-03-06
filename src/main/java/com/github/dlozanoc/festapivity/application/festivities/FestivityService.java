package com.github.dlozanoc.festapivity.application.festivities;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.dlozanoc.festapivity.application.domain.Festivity;
import com.github.dlozanoc.festapivity.application.domain.mappers.FromPersistentFestivityToDomainFestivityMapper;
import com.github.dlozanoc.festapivity.persistence.mappers.DomainFestivityToFestivityEntityMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FestivityService {

	@Autowired
	private FromPersistentFestivityToDomainFestivityMapper toDomainMapper;

	@Autowired
	private DomainFestivityToFestivityEntityMapper fromDomainMapper;

	@Autowired
	private FestivityRepository festivityRepository;

	public Optional<List<Festivity>> getAll() {
		try {
			return Optional.of(toDomainMapper.apply(
					StreamSupport.stream(festivityRepository.findAll().spliterator(), false).collect(Collectors.toList())));
		} catch (Exception e) {
			log.error("", e);
			return Optional.empty();
		}
	}

	public boolean save(Festivity festivity) {
		try {
			festivityRepository.save(fromDomainMapper.apply(festivity));
			return true;
		} catch (Exception e) {
			log.error("", e);
			return false;
		}
	}
	
	public Optional<Festivity> getById(Long id) {
		try {
			return Optional.of(toDomainMapper.apply(festivityRepository.findOne(id)));
		} catch (Exception e) {
			log.error("", e);
			return Optional.empty();
		}
	}
	
	public Optional<List<Festivity>> findBy(String name, ZonedDateTime startDate, ZonedDateTime endDate, String place) {
		
		return Optional.of(new ArrayList<>());
	}
}
