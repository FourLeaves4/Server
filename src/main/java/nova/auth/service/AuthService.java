package nova.auth.service;

import nova.auth.dto.MajorRequestDto;
import nova.auth.dto.MajorResponseDto;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public MajorResponseDto calculateSurveyResult(MajorRequestDto response) {
        int[] answers = response.getAnswers();

        return new MajorResponseDto(recommendMajor(answers));
    }

    private int recommendMajor(int[] num) {
        if (num[3] != 1) {
            return 5;
        }
        // 전공 추천 로직
        switch (num[0]) {
            case 1: case 3: // 웹 개발
                if (num[2] == 1 || num[2] == 2) {
                    return (num[1] == 1) ? 1 : 2;
                }
                break;
            case 2: // 앱 개발
                if (num[2] == 3 || num[2] == 4) {
                    return (num[2] == 3) ? 3 : 4;
                }
                break;
            default: // 잘 모르겠어요
                if (num[1] + num[2] == 2) {
                    return 1;
                } else if (num[1] + num[2] == 4) {
                    return (num[2] == 2) ? 2 : (num[2] == 4) ? 4 : 3;
                }
        }
        return 5;
    }
}