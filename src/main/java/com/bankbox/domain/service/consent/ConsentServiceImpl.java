package com.bankbox.domain.service.consent;

import com.bankbox.domain.entity.Consent;
import com.bankbox.domain.exception.ConsentMustHaveAtLeastOneRoleException;
import com.bankbox.infra.repository.ConsentRepository;
import com.bankbox.infra.repository.ConsentRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsentServiceImpl implements ConsentService {

  private final ConsentRepository consentRepository;
  private final ConsentRoleRepository consentRoleRepository;

  @Override
  public void addConsent(Consent consent) {
    validateConsentRolesAreValid(consent);
    consentRepository.save(consent);
  }

  private void validateConsentRolesAreValid(Consent consent) {
    if (consent.getRoles().isEmpty()) {
      throw new ConsentMustHaveAtLeastOneRoleException();
    }

    consent.getRoles().forEach(role -> {
      if (!consentRoleRepository.existsById(role.getId())) {
        throw new IllegalArgumentException("Invalid consent role: " + role.getId());
      }
    });
  }
}
