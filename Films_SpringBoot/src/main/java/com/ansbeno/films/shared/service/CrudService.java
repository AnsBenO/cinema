package com.ansbeno.films.shared.service;

import com.ansbeno.films.shared.base.BaseDto;

public interface CrudService<Dto extends BaseDto, Id> {

	void save(Dto t);

	Dto getOne(Id id);

	PagedResultDto<Dto> getAll(int pageNumber, String keyword, String t);

	boolean delete(Id id);
}
