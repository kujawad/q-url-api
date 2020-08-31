package com.qurlapi.qurlapi.dto.request;

import com.qurlapi.qurlapi.dto.GenericQUrlDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class QUrlRequest extends GenericQUrlDto {

}
