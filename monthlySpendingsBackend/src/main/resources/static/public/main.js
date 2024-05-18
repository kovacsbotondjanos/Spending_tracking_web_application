function submitDeleteForm(anchor) {
    if (confirm('Are you sure you want to delete this item?')) {
        const form = anchor.closest('form');
        fetch(form.action, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: new URLSearchParams(new FormData(form))
        })
        .then(response => {
            if (response.ok) {
                form.closest('tr').remove();
            } else {
                alert('Failed to delete the item.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Failed to delete the item.');
        });
    }
}