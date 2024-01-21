export default {
    toUpperCaseFirst(word) {
        if (word.length >= 1) {
            return word.charAt(0).toUpperCase() + word.slice(1);
        }
        return word;
    },

    getNumberFormatted(price) {
        return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    }
}