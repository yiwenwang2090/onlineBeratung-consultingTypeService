package de.caritas.cob.consultingtypeservice.api.controller;

import static de.caritas.cob.consultingtypeservice.testHelper.PathConstants.PATH_GET_BASIC_CONSULTING_TYPE_LIST;
import static de.caritas.cob.consultingtypeservice.testHelper.PathConstants.PATH_GET_EXTENDED_CONSULTING_TYPE_BY_ID;
import static de.caritas.cob.consultingtypeservice.testHelper.PathConstants.PATH_GET_FULL_CONSULTING_TYPE_BY_ID;
import static de.caritas.cob.consultingtypeservice.testHelper.PathConstants.PATH_GET_FULL_CONSULTING_TYPE_BY_SLUG;
import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.caritas.cob.consultingtypeservice.api.mapper.ExtendedConsultingTypeMapper;
import de.caritas.cob.consultingtypeservice.api.mapper.FullConsultingTypeMapper;
import de.caritas.cob.consultingtypeservice.api.model.BasicConsultingTypeResponseDTO;
import de.caritas.cob.consultingtypeservice.api.model.ExtendedConsultingTypeResponseDTO;
import de.caritas.cob.consultingtypeservice.api.service.ConsultingTypeService;
import de.caritas.cob.consultingtypeservice.testHelper.HelperMethods;
import java.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(ConsultingTypeController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ConsultingTypeConstrollerIT {

  @Autowired
  private MockMvc mvc;
  @MockBean
  private ConsultingTypeService consultingTypeService;

  @Test
  public void getBasicConsultingTypeList_Should_ReturnNoContent_When_ServiceReturnsEmptyList()
      throws Exception {

    when(consultingTypeService.fetchBasicConsultingTypesList())
        .thenReturn(null);

    mvc.perform(
        get(PATH_GET_BASIC_CONSULTING_TYPE_LIST)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());
  }

  @Test
  public void getBasicConsultingTypeList_Should_ReturnConsultingTypeBasicList()
      throws Exception {

    when(consultingTypeService.fetchBasicConsultingTypesList())
        .thenReturn(Arrays.asList(new BasicConsultingTypeResponseDTO().id(0),
            new BasicConsultingTypeResponseDTO().id(1),
            new BasicConsultingTypeResponseDTO().id(3)));

    mvc.perform(
        get(PATH_GET_BASIC_CONSULTING_TYPE_LIST)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  public void getFullConsultingTypeById_Should_ReturnFullConsultingTypeDTO() throws Exception {

    Integer consultingTypeId = 1;
    when(consultingTypeService.fetchFullConsultingTypeSettingsById(consultingTypeId))
        .thenReturn(FullConsultingTypeMapper
            .mapConsultingType(HelperMethods.getConsultingType()));

    mvc.perform(
        get(String.format(PATH_GET_FULL_CONSULTING_TYPE_BY_ID, consultingTypeId))
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(json().isEqualTo(HelperMethods.getConsultingTypeSettingsAsJsonString()));
  }

  @Test
  public void getFullConsultingTypeBySlug_Should_ReturnFullConsultingTypeDTO() throws Exception {

    String consultingTypeSlug = "consultingtype0";
    when(consultingTypeService.fetchFullConsultingTypeSettingsBySlug(consultingTypeSlug))
        .thenReturn(FullConsultingTypeMapper
            .mapConsultingType(HelperMethods.getConsultingType()));

    mvc.perform(
        get(String.format(PATH_GET_FULL_CONSULTING_TYPE_BY_SLUG, consultingTypeSlug))
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(json().isEqualTo(HelperMethods.getConsultingTypeSettingsAsJsonString()));
  }

  @Test
  public void getExtendedConsultingTypeById_Should_ReturnFullConsultingTypeDTO() throws Exception {

    Integer consultingTypeId = 1;
    ExtendedConsultingTypeResponseDTO extendedConsultingTypeResponseDTO = ExtendedConsultingTypeMapper
        .mapConsultingType(HelperMethods.getConsultingType());
    when(consultingTypeService.fetchExtendedConsultingTypeSettingsById(consultingTypeId))
        .thenReturn(extendedConsultingTypeResponseDTO);

    mvc.perform(
        get(String.format(PATH_GET_EXTENDED_CONSULTING_TYPE_BY_ID, consultingTypeId))
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(json()
            .isEqualTo(new ObjectMapper().writeValueAsString(extendedConsultingTypeResponseDTO)));
  }

}