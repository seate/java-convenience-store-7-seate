package store.global.dto.request;

import java.util.UUID;

public record PayRequestDTO(UUID uuid, Boolean isMembershipApplied) {
}
