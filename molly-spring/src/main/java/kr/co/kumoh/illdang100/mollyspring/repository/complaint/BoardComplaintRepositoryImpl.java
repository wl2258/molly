package kr.co.kumoh.illdang100.mollyspring.repository.complaint;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;

import java.util.List;

import static kr.co.kumoh.illdang100.mollyspring.domain.complaint.QBoardComplaint.boardComplaint;
import static kr.co.kumoh.illdang100.mollyspring.dto.admin.AdminRespDto.*;

public class BoardComplaintRepositoryImpl implements BoardComplaintRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BoardComplaintRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Slice<RetrieveComplaintListDto> searchSlice(Pageable pageable) {

        JPAQuery<RetrieveComplaintListDto> query = queryFactory
                .select(Projections.constructor(RetrieveComplaintListDto.class,
                        boardComplaint.id,
                        boardComplaint.reporterEmail,
                        boardComplaint.reportedEmail,
                        boardComplaint.createdDate))
                .from(boardComplaint)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1);

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(
                    boardComplaint.getType(), boardComplaint.getMetadata()
            );
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        List<RetrieveComplaintListDto> result = query.fetch();

        boolean hasNext = false;
        if (result.size() > pageable.getPageSize()) {
            result.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(result, pageable, hasNext);
    }
}
