package com.team7.ticket_booth.service;

import com.team7.ticket_booth.dto.request.ShowRequestDTO;
import com.team7.ticket_booth.dto.response.ShowResponseDTO;
import com.team7.ticket_booth.exception.NotFoundException;
import com.team7.ticket_booth.exception.RequestException;
import com.team7.ticket_booth.model.*;
import com.team7.ticket_booth.model.enums.Shift;
import com.team7.ticket_booth.repository.HallRepository;
import com.team7.ticket_booth.repository.MovieRepository;
import com.team7.ticket_booth.repository.ShowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShowService {
    private final ShowRepository showRepository;
    private final MovieRepository movieRepository;
    private final HallRepository hallRepository;

    @Transactional
    public List<Show> createShow(ShowRequestDTO dto) {
        Movie movie = movieRepository.findByTitle(dto.getMovieTitle())
                .orElseThrow(() -> new RequestException("Movie not found"));
        List<Show> shows = new ArrayList<>();
        List<Shift> shifts = dto.getStartTimes().stream().map(Shift::findByBeginTime).toList();
        for (LocalDate movieDate = movie.getBeginDay();
             !movieDate.isAfter(movie.getEndDay());
             movieDate = movieDate.plusDays(1)) {
            for (Shift shift : shifts) {
                List<Hall> availableHalls = hallRepository.findAllAvailableHalls(shift, movieDate);
                if (availableHalls.isEmpty()) {
                    throw new RequestException("No available hall for " + movieDate + " " + shift);
                }
                Hall hall = availableHalls.get(0); // hoặc chọn theo tiêu chí khác
                Show show = new Show(null, movie, hall, shift, movieDate, new ArrayList<>());
                shows.add(showRepository.save(show));
            }
        }
        return shows;
    }

    public ShowResponseDTO getShowById(UUID id) {
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Show not found"));
        return new ShowResponseDTO(show);
    }

    public List<ShowResponseDTO> getAllShowsOrderByTime(int page) {
        Pageable pageable = PageRequest.of(page, 20);
        Page<Show> shows = showRepository.findAllByShowDateAndShiftAsc(pageable);
        if (shows.isEmpty()) {
            throw new RequestException("No shows found");
        }
        return shows.stream().map(ShowResponseDTO::new).toList();
    }

    public List<ShowResponseDTO> getShowsByMovieId(UUID id) {
        List<Show> shows = showRepository.findByMovieIdOrderByShowDateAscShiftAsc(id);
        if (shows.isEmpty()) {
            throw new RequestException("No shows found for this movie");
        }
        return shows.stream().map(ShowResponseDTO::new).toList();
    }

    public Map<LocalDate, Map<String, List<ShowResponseDTO>>> getGroupedShowsByMovieId(UUID movieId) {
        List<ShowResponseDTO> shows = getShowsByMovieId(movieId);
        return shows.stream()
                .sorted(Comparator.comparing(ShowResponseDTO::getShowDate)
                        .thenComparing(ShowResponseDTO::getStartTime))
                .collect(Collectors.groupingBy(
                        ShowResponseDTO::getShowDate,
                        TreeMap::new,
                        Collectors.groupingBy(
                                ShowResponseDTO::getHallName,
                                TreeMap::new,
                                Collectors.toList()
                        )
                ));
    }
}
