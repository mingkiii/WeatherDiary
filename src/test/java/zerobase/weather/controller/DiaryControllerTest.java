package zerobase.weather.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import zerobase.weather.domain.Diary;
import zerobase.weather.service.DiaryService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DiaryController.class)
class DiaryControllerTest {
    @MockBean
    private DiaryService diaryService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void successCreateDiary() throws Exception {
        //given
        String date = "2023-01-01";
        String text = "This is a test diary.";
        //when
        doNothing().when(diaryService).createDiary(any(LocalDate.class), any(String.class));

        //then
        mockMvc.perform(post("/create/diary")
                        .param("date", date)
                        .content(text)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void readDiaryTest() throws Exception {
        //given
        LocalDate date = LocalDate.of(2023, 1, 1);
        Diary diary = new Diary();
        diary.setDate(date);
        diary.setText("Sample text");

        //when
        when(diaryService.readDiary(date)).thenReturn(Arrays.asList(diary));

        //then
        mockMvc.perform(get("/read/diary")
                        .param("date", date.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].text").value("Sample text"));
    }

    @Test
    public void readDiariesTest() throws Exception {
        //given
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 5, 20);
        Diary diary1 = new Diary();
        diary1.setDate(LocalDate.of(2023,2,2));
        diary1.setText("Sample text 1");
        Diary diary2 = new Diary();
        diary2.setDate(LocalDate.of(2023,5,5));
        diary2.setText("Sample text 2");

        //when
        when(diaryService.readDiaries(startDate, endDate)).thenReturn(Arrays.asList(diary1, diary2));

        //then
        mockMvc.perform(get("/read/diaries")
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].text").value("Sample text 1"))
                .andExpect(jsonPath("$[1].text").value("Sample text 2"));
    }

    @Test
    public void updateDiaryTest() throws Exception {
        //given
        LocalDate date = LocalDate.of(2023, 1, 1);
        String text = "Updated text";
        //when
        //then
        mockMvc.perform(put("/update/diary")
                        .param("date", date.toString())
                        .content(text)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteDiaryTest() throws Exception {
        //given
        LocalDate date = LocalDate.of(2023, 1, 1);
        //when
        //then
        mockMvc.perform(delete("/delete/diary")
                        .param("date", date.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}