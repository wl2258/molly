package kr.co.kumoh.illdang100.mollyspring.repository.complaint;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.kumoh.illdang100.mollyspring.dto.admin.AdminRespDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import java.util.List;

import static kr.co.kumoh.illdang100.mollyspring.domain.complaint.QCommentComplaint.commentComplaint;

public class CommentComplaintRepositoryImpl implements CommentComplaintRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public CommentComplaintRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Slice<AdminRespDto.RetrieveComplaintListDto> searchSlice(Pageable pageable) {

        JPAQuery<AdminRespDto.RetrieveComplaintListDto> query = queryFactory
                .select(Projections.constructor(AdminRespDto.RetrieveComplaintListDto.class,
                        commentComplaint.id,
                        commentComplaint.reporterEmail,
                        commentComplaint.reportedEmail,
                        commentComplaint.createdDate))
                .from(commentComplaint)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1);

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(
                    commentComplaint.getType(), commentComplaint.getMetadata()
            );
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        List<AdminRespDto.RetrieveComplaintListDto> result = query.fetch();

        boolean hasNext = false;
        if (result.size() > pageable.getPageSize()) {
            result.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(result, pageable, hasNext);
    }
}
