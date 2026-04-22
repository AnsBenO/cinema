package ntt.beca.films.dashboard;

import lombok.Builder;

@Builder
public record DashboardDataDto(
            long totalFilms,
            long totalGenres,
            long totalUsers,
            long totalScreenings,
            double averageRating) {

}
