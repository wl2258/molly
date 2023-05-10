package kr.co.kumoh.illdang100.mollyspring.repository.board;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.kumoh.illdang100.mollyspring.domain.board.BoardEnum;
import kr.co.kumoh.illdang100.mollyspring.domain.pet.PetTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;

import java.util.List;

import static kr.co.kumoh.illdang100.mollyspring.domain.account.QAccount.account;
import static kr.co.kumoh.illdang100.mollyspring.domain.board.QBoard.board;
import static kr.co.kumoh.illdang100.mollyspring.dto.board.BoardReqDto.*;
import static kr.co.kumoh.illdang100.mollyspring.dto.board.BoardRespDto.*;

public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BoardRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<SearchPostListDto> findPagePostList(RetrievePostListCondition retrievePostListCondition, Pageable pageable) {

        JPAQuery<SearchPostListDto> query = queryFactory
                .select(Projections.constructor(SearchPostListDto.class,
                        board.boardTitle,
                        account.nickname,
                        board.createdDate,
                        board.boardContent,
                        board.views,
                        board.commentCnt,
                        board.likyCnt,
                        board.hasImage,
                        board.isNotice))
                .from(board)
                .join(board.account, account)
                .where(categoryEq(retrievePostListCondition.getCategory()),
                        board.petType.eq(PetTypeEnum.valueOf(retrievePostListCondition.getPetType())))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(board.isNotice.desc());

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(
                    board.getType(), board.getMetadata()
            );
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        List<SearchPostListDto> content = query.fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(board.count())
                .from(board)
                .where(categoryEq(retrievePostListCondition.getCategory()),
                        board.petType.eq(PetTypeEnum.valueOf(retrievePostListCondition.getPetType())));


        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression categoryEq(String category) {
        return category.equals("ALL") ? null : board.category.eq(BoardEnum.valueOf(category));
    }
}
