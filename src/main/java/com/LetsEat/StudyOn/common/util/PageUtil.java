package com.LetsEat.StudyOn.common.util;

import com.LetsEat.StudyOn.common.exception.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import static com.LetsEat.StudyOn.common.exception.ErrorType.*;

public final class PageUtil {

    /**
     * 사용자가 요청한 페이지의 존재여부와 페이지 요소의 존재여부를 검증합니다.
     *
     * @param pageable 사용자가 요청한 Pageable 객체
     * @param page     Page 객체
     */
    public static void validatePageableWithPage(Pageable pageable, Page<?> page) {
        // 페이지의 요소가 없는 경우
        if (page.getTotalElements() == 0) {
            throw new CustomException(EMPTY_PAGE_ELEMENTS);
        }
        // 요청한 페이지가 존재하지 않을 경우
        if (pageable.getPageNumber() >= page.getTotalPages()) {
            throw new CustomException(PAGE_NOT_FOUND);
        }
    }

    /**
     * slice 객체의 페이지 요소 검증
     * @param slice slice 객체
     */
    public static void validateNonEmptySlice(Slice<?> slice) {
        // 페이지 요소가 없는 경우
        if (slice.isEmpty()) {
            throw new CustomException(EMPTY_PAGE_ELEMENTS);
        }
    }
}
