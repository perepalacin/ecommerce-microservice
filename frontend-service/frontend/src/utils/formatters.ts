export function priceFormatter(num: number) {
    if (isNaN(num)) return '';
    return num.toFixed(2).replace('.', ',').replace(/\B(?=(\d{3})+(?!\d))/g, '.');
  }