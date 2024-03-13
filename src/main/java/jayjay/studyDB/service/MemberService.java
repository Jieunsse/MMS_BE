package jayjay.studyDB.service;

import jayjay.studyDB.dto.MemberDTO;
import jayjay.studyDB.entity.MemberEntity;
import jayjay.studyDB.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service // 스프링 빈으로 등록 (어노테이션에 의해서)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public void save(MemberDTO memberDTO) {
        // 1. DTO -> Entity 객체로 변환
        // 2. repository 의 save 메서드 호출.
        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        memberRepository.save(memberEntity);
        // repository 의 save 메서드 호출 (조건. entity 객체를 넘겨줘야 함.)
    }



    public MemberDTO login(MemberDTO memberDTO) {
        /*
            1. 회원이 입력한 이메일로 DB 에서 조회를 함
            2. DB 에서 조회한 비밀번호와 사용자가 입력한 비밀번호가 일치하는지 판단
         */
        Optional<MemberEntity> byMemberEmail = memberRepository.findByMemberEmail(memberDTO.getMemberEmail());
        if (byMemberEmail.isPresent()) {
            // 조회 결과가 있다(해당 이메일을 가진 회원 정보가 있다)
            MemberEntity memberEntity = byMemberEmail.get();
            if (memberEntity.getMemberPassword().equals(memberDTO.getMemberPassword())) {
                // 비밀번호 일치
                // entity -> dto 변환 후 리턴
                return MemberDTO.toMemberDTO(memberEntity);
            } else {
                // 비밀번호 불일치(로그인실패)
                return null;
            }
        } else {
            // 조회 결과가 없다(해당 이메일을 가진 회원이 없다)
            return null;
        }
    }

    public List<MemberDTO> findAll() {
        // 엔티티 객체에서 DTO 객체로!
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        List<MemberDTO> memberDTOList = new ArrayList<>();
        for (MemberEntity memberEntity: memberEntityList) {
            memberDTOList.add(MemberDTO.toMemberDTO(memberEntity));
        }
        return memberDTOList;
    }

    public MemberDTO findById(Long id) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(id);
        // 결과물을 컨트롤러로 리턴
        return optionalMemberEntity.map(MemberDTO::toMemberDTO).orElse(null);
    }

    public MemberDTO updateForm(String myEmail) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(myEmail);
        // optional 객체를 까서, DTO 로 변환해서 리턴하기!
        return optionalMemberEntity.map(MemberDTO::toMemberDTO).orElse(null);
    }

    public void update(MemberDTO memberDTO) {
        memberRepository.save(MemberEntity.toUpdateMemberEntity(memberDTO)); // 여기서 toMemberEntity 메소드를 사용하면 update 가 아닌 insert 가 되어버린다. 주의!
    }

    public void deleteById(Long id) {
        memberRepository.deleteById(id);
    }
}

