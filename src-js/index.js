// var goog = goog || {global = widdow};

import React from 'react';
import ReactDOM from 'react-dom';
window.React = React;
window.ReactDOM = ReactDOM;

import {
    Button,
    Row,
    Col,
    Layout,
    Card,
    LocaleProvider,
    Table,
    Menu,
    Spin,
    Modal,
    Typography,
    Pagination,
    Breadcrumb,
    Icon,
    Input,
    InputNumber,
    Form,
    Dropdown,
    Checkbox,
    Cascader,
    DatePicker,
    Radio,
    Select,
    TreeSelect,
    Timeline,
    TimePicker,
    Upload,
    Carousel,
    Tree,
    Tabs,
    Alert,
    message,
    notification,
    Skeleton,
    BackTop,
    Divider
} from 'antd';
import zhCN from 'antd/lib/locale-provider/zh_CN';
// window.antd = antd;
window.Divider = Divider;
window.BackTop = BackTop;
window.Skeleton = Skeleton;
window.notifacation = notification;
window.message = message;
window.Alert = Alert;
window.Tabs = Tabs;
window.Tree = Tree;
window.Carousel = Carousel;
window.Upload = Upload;
window.TimePicker = TimePicker;
window.Timeline = Timeline;
window.TreeSelect = TreeSelect;
window.Select = Select;
window.Radio = Radio;
window.DatePicker = DatePicker;
window.Cascader = Cascader;
window.Checkbox = Checkbox;
window.Dropdown = Dropdown;
window.Button = Button;
window.Row = Row;
window.Col = Col;
window.Layout = Layout;
window.Menu = Menu;
window.Pagination = Pagination;
window.Card = Card;
window.LocalProvider = LocaleProvider;
window.Table = Table;
window.Spin = Spin;
window.Modal = Modal;
window.Typegraphy = Typography;
window.Breadcrumb = Breadcrumb;
window.Icon = Icon;
window.Input = Input;
window.InputNumber = InputNumber;
window.Form = Form;

import './default.less';

import moment from 'moment';
import 'moment/locale/zh-cn';
window.moment = moment;

import hljs from 'highlight.js';
window.hljs = hljs;

import showdown from 'showdown';
window.showdown = showdown;

import SimpleMDE from 'simplemde';
window.SimpleMDE = SimpleMDE;

moment.locale('zh-cn');
