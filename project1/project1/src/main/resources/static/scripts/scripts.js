document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('create-item-form');
    if (form) {
        form.addEventListener('submit', async (e) => {
            e.preventDefault();
            const formData = new FormData(form);
            const data = {
                name: formData.get('name'),
                description: formData.get('description'),
                price: parseFloat(formData.get('price'))
            };

            try {
                const response = await fetch('/api/items', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(data)
                });

                if (response.ok) {
                    alert('Item created successfully!');
                    window.location.href = '/items';
                } else {
                    const errorData = await response.json();
                    alert(`Failed to create item: ${errorData.message}`);
                }
            } catch (error) {
                console.error('Error:', error);
                alert('Failed to create item. See console for details.');
            }
        });
    }
});
