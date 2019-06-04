(ns soul-talk.components.antd-helper
  (:require [antd]))

(def components '[Affix
                  Alert
                  Anchor
                  Anchor.Link
                  AutoComplete
                  AutoComplete.OptGroup
                  AutoComplete.Option
                  Avatar
                  BackTop
                  Badge
                  Breadcrumb
                  Breadcrumb.Item
                  Button
                  Button.Group
                  Calendar
                  Card
                  Card.Grid
                  Card.Meta
                  Carousel
                  Cascader
                  Checkbox
                  Checkbox.Group
                  Col
                  Collapse
                  Collapse.Panel
                  DatePicker
                  DatePicker.MonthPicker
                  DatePicker.RangePicker
                  DatePicker.WeekPicker
                  Divider
                  Drawer
                  Dropdown
                  Dropdown.Button
                  Form
                  Form.Item
                  Icon
                  Input
                  Input.Group
                  Input.Search
                  Input.TextArea
                  InputNumber
                  Layout
                  Layout.Content
                  Layout.Footer
                  Layout.Header
                  Layout.Sider
                  List
                  List.Item
                  List.Item.Meta
                  LocaleProvider
                  Mention
                  Mention.Nav
                  Menu
                  Menu.Divider
                  Menu.Item
                  Menu.ItemGroup
                  Menu.SubMenu
                  Modal
                  Pagination
                  Popconfirm
                  Popover
                  Progress
                  Radio
                  Radio.Button
                  Radio.Group
                  Rate
                  Row
                  Select
                  Select.OptGroup
                  Select.Option
                  Slider
                  Spin
                  Steps
                  Steps.Step
                  Switch
                  Table
                  Table.Column
                  Table.ColumnGroup
                  Tabs
                  Tabs.TabPane
                  Tag
                  Tag.CheckableTag
                  TimePicker
                  Timeline
                  Timeline.Item
                  Tooltip
                  Transfer
                  Tree
                  TreeSelect
                  TreeSelect.TreeNode
                  Tree.TreeNode
                  Upload])

(def props '[locales])

(def funcs '[message.config
             message.destroy
             message.error
             message.info
             message.loading
             message.success
             message.warn
             message.warning
             Modal.confirm
             Modal.error
             Modal.info
             Modal.success
             Modal.warning
             notification.close
             notification.config
             notification.destroy
             notification.error
             notification.info
             notification.open
             notification.success
             notification.warn
             notification.warning])

(def form-funcs '[getFieldDecorator
                  getFieldError
                  getFieldsError
                  getFieldsValue
                  getFieldValue
                  isFieldsTouched
                  isFieldTouched
                  isFieldValidating
                  resetFields
                  setFields
                  setFieldsValue
                  validateFields
                  validateFieldsAndScroll])
