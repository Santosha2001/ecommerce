import React from 'react'
import '../../../styles/pagination.css';

export const Pagination = ({ currentPage, totalPages, onPageChange }) => {

    const pageNumbers = [];
    for (let i = 1; i <= totalPages; i++) {
        pageNumbers.push(i);
    }

    return (
        <div className="pagination">
            {pageNumbers.map((number) => (
                <button
                    key={number}
                    onClick={() => onPageChange(number)}
                    className={number === currentPage ? 'active' : ''}
                >
                    {number}

                </button>
            ))}
        </div>
    )
};
export default Pagination;
