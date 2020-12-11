// Обработчик клика
export const collapseClickHandler = (event, setActive) => {
    // Определение вызывающего элемента
    const triggerEl = getTriggerElement(event.target);
    // Если у элемента и его родителя нет атрибута
    if (triggerEl === undefined) {
        // Отменяем действие
        event.preventDefault();
        return false;
    }

    // Получаем целевой элемент
    const targetEl = document.querySelector(triggerEl.getAttribute("data-target"));
    // Если целевой элемент существует
    if (targetEl) {
        setActive(triggerEl);

        // Манипулируем классами
        targetEl.classList && targetEl.classList.toggle("-on");
        triggerEl.classList.toggle("-active");
    }
};

const getTriggerElement = (el) => {
    // Получаем атрибут `data-collapse`
    const isCollapse = el.getAttribute("data-collapse");
    // Если атрибут существует, то
    if (isCollapse !== null) {
        // Возвращаем элемент на котором осуществлен клик
        return el;
    } else {
        // Иначе пытаемся найти атрибут у его родителя
        const isParentCollapse = el.parentNode.getAttribute("data-collapse");
        // Возвращаем родительский элемент или undefined
        return (isParentCollapse !== null)
            ? el.parentNode
            : undefined;
    }
};
