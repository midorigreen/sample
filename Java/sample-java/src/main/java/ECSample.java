import java.util.List;
import java.util.Map;
import org.eclipse.collections.api.LazyIterable;
import org.eclipse.collections.api.RichIterable;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.multimap.Multimap;
import org.eclipse.collections.api.partition.list.PartitionImmutableList;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Maps;
import org.eclipse.collections.impl.list.mutable.FastList;


import static org.eclipse.collections.impl.factory.Lists.immutable;
import static org.eclipse.collections.impl.factory.Lists.mutable;

/**
 * Created by midori on 2017/02/16.
 */
public class ECSample {

	public void samples() {

		// ============================
		// List
		// ============================
		List<Integer> l1 = FastList.newList();
		List<Integer> l2 = FastList.newListWith(1, 2, 3, 4);
		List<Integer> l3 = Lists.mutable.of(1, 2, 3);
		// Type must be ImmutableList<>
		ImmutableList<Integer> l4 = Lists.immutable.of();

		/**
		 * select(Predicate<? super T> predicate)
		 * return List<T>
		 */
		{
			immutable
					.of(1, 2, 3, 4, 5)
					.select(x -> x > 3)
					.forEach(System.out::print); // 45
			seperateLine();
		}

		/**
		 * reject(Predicate<? super T> predicate)
		 * return List<T>
		 */
		{
			immutable
					.of(1, 2, 3, 4, 5)
					.reject(x -> x > 3)
					.forEach(System.out::print); //123
			seperateLine();
		}

		/**
		 * partition(Predicate<? super T> predicate)
		 * return PartitionList<T>
		 */
		{
			PartitionImmutableList<Integer> partitionList = immutable
					.of(1, 2, 3, 4, 5)
					.partition(x -> x > 3);
			System.out.println(partitionList.getSelected()); // [4, 5]
			System.out.println(partitionList.getRejected()); // [1, 2, 3]
			seperateLine();
		}

		/**
		 * collect(Function<? super T,? extends V> function)
		 * return <V,R extends Collection<V>> R
		 */
		{
			immutable
					.of("one", "two", "three", "four", "five")
					.collect(x -> x.length())
					.forEach(System.out::print); //33544
			seperateLine();
			System.out.println(
					createTrains()
							.collect(t -> t.getName()) // [nozomi, hikari, yamanote, ginza]
			);
			createTrains()
					.collect(t -> {
						String name = t.getName();
						return new Train(name);
					})
					.forEach(x -> {
						System.out.print(x.getId());
						System.out.print(" ,");
					}); // nozomi ,hikari ,yamanote ,ginza ,
			seperateLine();
		}

		/**
		 * flatCollect(Function<? super T,? extends Iterable<V>> function)
		 * XXX jdk -> flatMap
		 * return List<V>
		 */
		{
			immutable
					.of(createTrains(),
							createTrains())
					.flatCollect(ts -> ts.collect(t -> t.getId()))
					.forEach(System.out::print); //12341234
			seperateLine();
		}

		/**
		 * groupBy(Function<? super T,? extends V> function)
		 * return MultiMap<V, T>
		 */
		{
			System.out.println(
					immutable
							.of(1, 2, 3, 4, 5)
							.groupBy(x -> x % 2 == 0) //{false=[1, 3, 5], true=[2, 4]}
			);
			Multimap<Integer, Train> nameLenMap = createTrains()
					.groupBy(t -> t.getName().length());
			nameLenMap.forEachKey(System.out::print); //568
			seperateLine();
			nameLenMap.forEachValue(System.out::print); // Train{name='ginza'}Train{name='nozomi'}Train{name='hikari'}Train{name='yamanote'}
			seperateLine();
			nameLenMap.forEachKeyMultiValues((k, v) -> {
				System.out.print(k);
				System.out.print(",");
				System.out.print(v);
				System.out.print(" ");
			}); // 5,[Train{name='ginza'}] 6,[Train{name='nozomi'}, Train{name='hikari'}] 8,[Train{name='yamanote'}]
			seperateLine();
		}

		/**
		 * detect(Predicate<? super T> predicate)
		 * return T
		 */
		{
			ImmutableList<Integer> list = immutable.of(1, 2, 3, 4, 5);
			System.out.println(list.detect(x -> x.equals(3))); // 3
			System.out.println(list.detect(x -> x.equals(100))); // null
		}

		/**
		 * detectIfNone(Predicate<? super T> predicate, Function0<? extends T> defaultValueBlock)
		 * return T
		 */
		{
			ImmutableList<Integer> list = immutable.of(1, 2, 3, 4, 5);
			System.out.println(list.detectIfNone(x -> x.equals(3), () -> 10)); // 3
			System.out.println(list.detectIfNone(x -> x.equals(100), () -> 10)); // 10
		}

		/**
		 * anySatisfy(Predicate<? super T> predicate)
		 * return boolean
		 */
		{
			ImmutableList<Integer> list = immutable.of(1, 2, 3, 4, 5);
			System.out.println(list.anySatisfy(x -> x.equals(3))); // true
			System.out.println(list.anySatisfy(x -> x.equals(100))); // false
		}

		/**
		 * allSatisfy(Predicate<? super T> predicate)
		 * return boolean
		 */
		{
			ImmutableList<Integer> list = immutable.of(1, 2, 3, 4, 5);
			System.out.println(list.allSatisfy(x -> x < 10)); // true
			System.out.println(list.allSatisfy(x -> x > 10)); // false
		}

		/**
		 * noneSatisfy(Predicate<? super T> predicate)
		 * return boolean
		 */
		{
			ImmutableList<Integer> list = immutable.of(1, 2, 3, 4, 5);
			System.out.println(list.noneSatisfy(x -> x > 10)); // true
			System.out.println(list.noneSatisfy(x -> x < 10)); // false
		}

		/**
		 * each(Procedure<? super T> procedure)
		 * return void
		 * XXX not use forEach() because it is same name method in jdk
		 */
		{
			immutable
					.of(1, 2, 3, 4, 5)
					.each(System.out::print); // 12345
			seperateLine();
		}

		/**
		 * forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure)
		 * return void
		 */
		{
			FastList.newListWith(10, 20, 30).forEachWithIndex((v, i) -> {
				System.out.print(i);
				System.out.print(",");
				System.out.print(v);
				System.out.print(" ");
			});
			seperateLine();
		}

		/**
		 * injectInto(IV injectedValue, Function2<? super IV,? super T,? extends IV> function)
		 * return IV
		 */
		{
			System.out.println(
					Lists.mutable.of("1", "2", "3")
					.injectInto("", (memo, v) -> {
							return memo + "-" + v;
					})
			); // -1-2-3
		}

		/**
		 * tap(Procedure<? super T> procedure)
		 * return List<T>
		 */
		{
			immutable
					.of(1, 2, 3, 4, 5)
					.tap(System.out::print) // 12345
					.select(x -> x > 3)
					.each(System.out::print); // 45
			seperateLine();
		}

		/**
		 * Lazy Iteration
		 */
		{
			// normal
			ImmutableList<Integer> list = immutable.of(1, 2, 3, 4, 5);
			lazyTest(list);
			/** print
			 1. select x > 2
			 1. exec
			 1. exec
			 1. exec
			 1. exec
			 1. exec
			 2. collect +1
			 2. exec
			 2. exec
			 2. exec
			 3. each print
			 345
			 */

			// lazy
			LazyIterable<Integer> listLazy = list.asLazy();
			lazyTest(listLazy);
			/**
			 1. select x > 2
			 2. collect +1
			 3. each print
			 1. exec
			 1. exec
			 1. exec
			 2. exec
			 31. exec <- 3
			 2. exec
			 41. exec <- 4
			 2. exec
			 5 <- 5
			 */
		}

		/**
		 * makeString()
		 * return String
		 */
		{
			RichIterable<Integer> list = immutable.of(1, 2, 3, 4, 5);
			System.out.println(list.makeString()); // 1, 2, 3, 4, 5
			System.out.println(list.makeString(".")); // 1.2.3.4.5
			System.out.println(list.makeString("{", ",", "}")); // 1.2.3.4.5
			seperateLine();
		}

		/**
		 * appendString(Appendable appendable)
		 * return void
		 */
		{
			RichIterable<Integer> list = immutable.of(1, 2, 3, 4, 5);
			StringBuilder sb = new StringBuilder();
			list.appendString(sb, "|");
			System.out.println(sb); // 1|2|3|4|5
			seperateLine();
		}

		/**
		 * count(Predicate<? super T> predicate)
		 * return int
		 */
		{
			RichIterable<Integer> list = immutable.of(1, 2, 3, 4, 5);
			System.out.println(list.count(x -> x % 2 == 0)); // 2
			seperateLine();
		}

		/**
		 * getFirst(), getLast()
		 * return T
		 */
		{
			MutableList<Integer> list = mutable.of(1, 2, 3, 4, 5);
			System.out.println(list.getFirst()); // 1
			System.out.println(list.getLast()); // 5
			seperateLine();
		}

		/**
		 * max()/min()
		 * return T
		 */
		{
			MutableList<Integer> list = Lists.mutable.of(1, 2, 3, 4, 5);
			System.out.println(list.max()); // 5
			System.out.println(list.min()); // 1
			seperateLine();
		}

		/**
		 * maxBy()/minBy()
		 * return T
		 */
		{
			MutableList<String> list = Lists.mutable.of("on", "two", "three");
			System.out.println(list.maxBy(x -> x.length())); // three
			System.out.println(list.minBy(x -> x.length())); // on
			seperateLine();
		}

		/**
		 * aggregateBy(Function<? super T,? extends K> groupBy,
		 *             Function0<? extends V> zeroValueFactory,
		 *             Function2<? super V,? super T,? extends V> nonMutatingAggregator)
		 * return MutableMap<K, V>
		 */
		{
			System.out.println(
					Lists.mutable
							.of(1, 2, 3, 4, 5)
					    .aggregateBy(
					    		n -> (n % 2 == 0) ? "E" : "O",
									() -> Lists.mutable.empty(),
									(list, n) -> {
										list.add(n);
										return list;
									}
							) // {E=[2, 4], O=[1, 3, 5]}
			);
			System.out.println(
					Lists.mutable
							.of("one", "two", "three")
							.aggregateBy(
									x -> x.length(),
									() -> Lists.mutable.empty(),
									(list, n) -> {
										list.add(n);
										return list;
									}
							) // {3=[one, two], 5=[three]}
			);
			seperateLine();
		}

		/**
		 * chunk(int size)
		 * return RichIterable<RichIterable>
		 */
		{
			System.out.println(
					Lists.mutable.of(1, 2, 3, 4, 5, 6, 7).chunk(3) // [[1, 2, 3], [4, 5, 6], [7]]
			);
			seperateLine();
		}

		/**
		 * zip(Iterable<S> that, R target)
		 * return List<Pair<K, V>>
		 */
		{
			List<Pair<Integer, String>> listPairs = Lists.mutable
					.of(1, 2, 3)
					.zip(Lists.mutable.of("one", "two", "three"));
			System.out.println(listPairs); // [1:one, 2:two, 3:three]
			seperateLine();
		}

		/**
		 * take/drop
		 * return List
		 */
		{
			MutableList<Integer> list = Lists.mutable.of(1, 2, 3, 4, 5);
			System.out.println(list.take(2)); // [1, 2]
			System.out.println(list.drop(2)); // [3, 4, 5]
			seperateLine();
		}

		/**
		 * distinct()
		 * return List
		 */
		{
			System.out.println(Lists.mutable.of(1, 1, 2, 3, 3, 4, 4, 5).distinct()); // [1, 2, 3, 4, 5]
		}

		/**
		 * with/withOut
		 * return List
		 */
		{
			Lists.mutable
					.of(1, 2, 3)
					.tap(System.out::print) // 123
					.with(4)
					.tap(System.out::print) // 1234
					.without(2)
					.each(System.out::print); // 134
		}
	}

	private void lazyTest(RichIterable<Integer> list) {
		System.out.println("1. select x > 2");
		RichIterable<Integer> biggerTwos = list.select(x -> {
			System.out.println("1. exec");
			return x > 2;
		});
		System.out.println("2. collect +1");
		RichIterable<Integer> plusOnes = biggerTwos.collect(x -> {
			System.out.println("2. exec");
			return x++;
		});
		System.out.println("3. each print");
		plusOnes.each(System.out::print);
		seperateLine();
	}


	private void seperateLine() {
		System.out.println();
		System.out.println("----------------------------------");
	}

	private ImmutableList<Train> createTrains() {
		return immutable
				.of(new Train("1", "nozomi"),new Train("2", "hikari"),
						new Train("3", "yamanote"),new Train("4", "ginza"));
	}

	private void print(Integer i) {
		MutableList<Integer> list = Lists.mutable.of(1, 2, 3, 4);
		list.collect(i -> i > 2);
	}
}
