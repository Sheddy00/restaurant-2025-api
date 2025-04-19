package edu.hei.school.restaurant.endpoint.rest;

import edu.hei.school.restaurant.model.enums.StatusEnum;

public record DishStatusUpdateRest(StatusEnum newStatus) {
}
