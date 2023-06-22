package hello.itemservice.repository.jpa;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaItemRepositoryV2 implements ItemRepository {

    private final SpringDataJpaItemRepository repository;

    @Override
    public Item save(final Item item) {
        return repository.save(item);
    }

    @Override
    public void update(final Long itemId, final ItemUpdateDto updateParam) {
        final Item findItem = findById(itemId).orElseThrow();
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    @Override
    public Optional<Item> findById(final Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Item> findAll(final ItemSearchCond cond) {
        final String itemName = cond.getItemName();
        final Integer maxPrice = cond.getMaxPrice();

        if (StringUtils.hasText(itemName) && maxPrice != null) {
            return repository.findItems(itemName, maxPrice);
        }
        if (StringUtils.hasText(itemName)) {
            return repository.findByItemNameLike(itemName);
        }
        if (maxPrice != null) {
            return repository.findByPriceLessThanEqual(maxPrice);
        }
        return repository.findAll();
     }
}
