// JavaScript para el filtrado en tiempo real
		document.addEventListener('DOMContentLoaded', function () {
			const searchInput = document.getElementById('searchInput');
			const foodCards = document.querySelectorAll('.food-card');

			searchInput.addEventListener('input', function () {
				const filter = searchInput.value.toLowerCase();

				foodCards.forEach(function (card) {
					const title = card.querySelector('.card-title').textContent.toLowerCase();
					const description = card.querySelector('.card-text').textContent.toLowerCase();

					if (title.includes(filter) || description.includes(filter)) {
						card.style.display = '';
					} else {
						card.style.display = 'none';
					}
				});
			});
		});