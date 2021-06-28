package com.iktpreobuka.project.entities.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {

	private String categoryName;

	private List<ReportItemDTO> reportItems;

	private Double sumOfIncomes;

	private Integer totalNumberOfSoldOffers;

}
