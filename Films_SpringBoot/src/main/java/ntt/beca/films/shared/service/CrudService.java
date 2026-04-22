package ntt.beca.films.shared.service;


public interface CrudService<O,Long> {
	
		void save(O t);
		O getOne(Long id);
		PagedResultDto<O> getAll(int pageNumber,String keyword,String t);
		boolean delete(Long id);
}
