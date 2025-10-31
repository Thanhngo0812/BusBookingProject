import React from 'react';
import Select from 'react-select';

// --- ĐÂY LÀ STYLE CHO COMPONENT react-select ---
// Nó định nghĩa style cho các phần tử bên trong
const customSelectStyles = {
  control: (provided, state) => ({
    ...provided,
    border: state.isFocused ? '1px solid #f56d04' : '1px solid #ccc',
    boxShadow: state.isFocused ? '0 0 0 1px #f56d04' : 'none',
    '&:hover': {
      borderColor: state.isFocused ? '#f56d04' : '#aaa',
    },
    fontSize: '16px',
    padding: '4px 0', 
    borderRadius: '8px',
    height: '40px'
  }),
  option: (provided, state) => ({
    ...provided,
    backgroundColor: state.isSelected ? '#f56d04' : state.isFocused ? '#ffeadd' : 'white',
    color: state.isSelected ? 'white' : 'black',
    '&:active': {
      backgroundColor: '#e65e00',
    },
  }),
  // Chúng ta không cần định nghĩa 'groupHeading' ở đây
  // vì sẽ dùng component 'formatGroupLabel' tùy chỉnh
};

// --- ĐÂY LÀ STYLE CHO TIÊU ĐỀ NHÓM ---
// Đây là một OBJECT style bình thường của React
const groupLabelStyles = {
  fontWeight: 'bold',
  color: '#333',
  fontSize: '14px',
  padding: '10px 12px 5px 12px',
  textTransform: 'uppercase'
};

/**
 * Component format tiêu đề nhóm (ví dụ: "TỈNH/THÀNH PHỐ")
 * Chúng ta render một <div> và áp dụng style object 'groupLabelStyles'
 */
const formatGroupLabel = (data) => (
  <div style={groupLabelStyles}>{data.label}</div> // <-- Lỗi đã được sửa ở đây
);

/**
 * Đây là component LocationSelect, một "wrapper" (lớp bọc)
 * cho react-select với các style đã được tùy chỉnh
 */
const LocationSelect = (props) => {
  return (
    <Select
      styles={customSelectStyles}      // Áp dụng style cho react-select
      formatGroupLabel={formatGroupLabel} // Dùng component tùy chỉnh để render tiêu đề
      placeholder="Chọn điểm..."
      noOptionsMessage={() => "Không tìm thấy"}
      {...props}
    />
  );
};

export default LocationSelect;