package zerobase.weather.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;
import zerobase.weather.domain.DateWeather;
import zerobase.weather.domain.Diary;
import zerobase.weather.error.InvalidDate;
import zerobase.weather.error.Nonexistent;
import zerobase.weather.repository.DateWeatherRepository;
import zerobase.weather.repository.DiaryRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DiaryServiceTest {
    @Mock
    private DiaryRepository diaryRepository;

    @Mock
    private DateWeatherRepository dateWeatherRepository;

    @InjectMocks
    private DiaryService diaryService;

    public DiaryServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createDiaryTest() {
        //given
        LocalDate date = LocalDate.of(2023, 1, 1);
        String text = "Sample text";
        DateWeather dateWeather = new DateWeather();
        dateWeather.setDate(date);

        //when
        when(dateWeatherRepository.findAllByDate(date)).thenReturn(Arrays.asList(dateWeather));

        diaryService.createDiary(date, text);
        //then
        verify(diaryRepository, times(1)).save(any(Diary.class));
    }

    @Test
    public void readDiaryTest() {
        //given
        LocalDate date = LocalDate.of(2023, 1, 1);
        Diary diary = new Diary();
        diary.setText("Sample text");

        //when
        when(diaryRepository.findAllByDate(date)).thenReturn(Arrays.asList(diary));

        //then
        List<Diary> result = diaryService.readDiary(date);

        assertEquals(1, result.size());
        assertEquals("Sample text", result.get(0).getText());
    }

    @Test
    public void readDiary_InvalidDateTest() {
        //given
        //when
        LocalDate date = LocalDate.of(3051, 1, 1);

        //then
        assertThrows(InvalidDate.class, () -> {
            diaryService.readDiary(date);
        });
    }

    @Test
    public void readDiariesTest() {
        //given
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 5, 20);
        Diary diary1 = new Diary();
        diary1.setText("Sample text 1");
        Diary diary2 = new Diary();
        diary2.setText("Sample text 2");

        //when
        when(diaryRepository.findAllByDateBetween(startDate, endDate)).thenReturn(Arrays.asList(diary1, diary2));

        //then
        List<Diary> result = diaryService.readDiaries(startDate, endDate);

        assertEquals(2, result.size());
        assertEquals("Sample text 1", result.get(0).getText());
        assertEquals("Sample text 2", result.get(1).getText());
    }

    @Test
    public void updateDiaryTest() {
        // given
        LocalDate date = LocalDate.of(2023, 1, 1);
        String oldText = "Old text";
        String newText = "New text";

        Diary existingDiary = new Diary();
        existingDiary.setDate(date);
        existingDiary.setText(oldText);
        when(diaryRepository.getFirstByDate(date)).thenReturn(existingDiary);
        when(diaryRepository.save(existingDiary)).thenReturn(existingDiary);

        // when
        Diary updatedDiary = diaryService.updateDiary(date, newText);

        // then
        assertEquals(newText, updatedDiary.getText());
    }

    @Test
    public void deleteDiaryTest() {
        //given
        LocalDate date = LocalDate.of(2023, 1, 1);
        Diary diary = new Diary();
        diary.setDate(date);
        //when
        when(diaryRepository.findAllByDate(date)).thenReturn(Collections.singletonList(diary));
        diaryService.deleteDiary(date);
        //then
        verify(diaryRepository, times(1)).deleteAllByDate(date);
    }
    @Test
    public void deleteDiary_NonexistentDiaryTest() {
        // given
        LocalDate date = LocalDate.of(2023, 1, 1);

        // Mock repository behavior
        when(diaryRepository.findAllByDate(date)).thenReturn(Collections.emptyList());

        // then
        assertThrows(Nonexistent.class, () -> {
            // when
            diaryService.deleteDiary(date);
        });
    }
}