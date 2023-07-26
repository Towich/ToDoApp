package com.example.todoapp.ui.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todoapp.App
import com.example.todoapp.R
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.ui.Activity.ui.theme.Blue
import com.example.todoapp.ui.Activity.ui.theme.ToDoAppTheme
import com.example.todoapp.ui.Activity.ui.theme.White
import com.example.todoapp.ui.ViewModel.EditTaskViewModel
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.time.LocalDate
import java.util.*
import javax.inject.Inject


class EditTaskFragment : Fragment() {

    // ViewModel Factory
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    // ViewModel
    private lateinit var viewModel: EditTaskViewModel

    // Is currently editing
    private var isCurrEditing: Boolean = false

    private lateinit var currModel: TodoItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inject
        (requireContext().applicationContext as App).appComponent.startComponent().create()
            .inject(this)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this, viewModelFactory)[EditTaskViewModel::class.java]

        isCurrEditing = viewModel.isCurrEditing()

        // Get current editing model
        currModel = viewModel.getCurrModel() ?: TodoItem(0, "", "", "", false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return ComposeView(requireContext()).apply {
            setContent {
                ToDoAppTheme {
                    EditTaskContent()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    @Preview(
        showBackground = true,
        showSystemUi = true,
        heightDp = 2000
    )
    @Composable
    fun ToDoAppPreview() {
        ToDoAppTheme {
            EditTaskContent()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun EditTaskContent() {
        val sheetState = rememberModalBottomSheetState()
        val scope = rememberCoroutineScope()
        var showBottomSheet by remember { mutableStateOf(false) }
        var currentImportance by remember { mutableStateOf("Нет") }

        if(isCurrEditing)
            currentImportance = currModel.importance

        Scaffold(
            topBar = {
                EditTaskTopBar()
            },
            content = {
                Column(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize(),
//                        .verticalScroll(rememberScrollState())
                    verticalArrangement = Arrangement.Top,
                ) {

                    // TextField
                    MTextField()

                    // Importance Button
                    ImportanceTextButton(
                        onClicked = { showBottomSheet = true },
                        currentImportance = currentImportance
                    )

                    // BottomSheet
                    if (showBottomSheet) {
                        ModalBottomSheet(
                            onDismissRequest = { showBottomSheet = false },
                            sheetState = sheetState
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {

                                ItemBottomSheet(
                                    textImportance = "Нет",
                                    onClicked = {
                                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                                            if (!sheetState.isVisible) {
                                                showBottomSheet = false
                                            }
                                        }
                                        currentImportance = "Нет"
                                        currModel.importance = currentImportance
                                    }
                                )

                                ItemBottomSheet(
                                    textImportance = "Низкий",
                                    onClicked = {
                                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                                            if (!sheetState.isVisible) {
                                                showBottomSheet = false
                                            }
                                        }
                                        currentImportance = "Низкий"
                                        currModel.importance = currentImportance
                                    }
                                )

                                ItemBottomSheet(
                                    textImportance = "Высокий",
                                    onClicked = {
                                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                                            if (!sheetState.isVisible) {
                                                showBottomSheet = false
                                            }
                                        }
                                        currentImportance = "Высокий"
                                        currModel.importance = currentImportance
                                    }
                                )
                            }
                        }
                    }

                    Divider(
                        modifier = Modifier
                            .padding(top = 20.dp, bottom = 20.dp, start = 28.dp, end = 28.dp),
                        color = MaterialTheme.colorScheme.tertiary
                    )

                    // Date picker with Switch
                    DateRow()

                    Divider(
                        modifier = Modifier
                            .padding(top = 50.dp, bottom = 20.dp),
                        color = MaterialTheme.colorScheme.tertiary
                    )

                    TextButton(
                        onClick = {
                            viewModel.removeTask(currModel)
                            viewModel.clearCurrModel()
                            parentFragment?.findNavController()?.navigateUp()
                        },
                        enabled = isCurrEditing,
                        modifier = Modifier
                            .padding(start = 15.dp)
                    ) {
                        Row{
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Delete Icon",
                                tint = if(isCurrEditing) Color.Red else MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier
                                    .padding(top = 5.dp)
                            )
                            Text(
                                text = "Удалить",
                                color = if(isCurrEditing) Color.Red else MaterialTheme.colorScheme.tertiary,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier
                                    .padding(start = 15.dp)
                            )
                        }

                    }

                }
            })
    }

    @Composable
    fun ImportanceTextButton(onClicked: () -> Unit, currentImportance: String) {
        TextButton(
            onClick = onClicked,
            modifier = Modifier
                .padding(top = 5.dp, start = 15.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 10.dp)
            ) {
                Text(
                    text = "Важность",
                    color = MaterialTheme.colorScheme.onSecondary,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = currentImportance,
                    color = MaterialTheme.colorScheme.tertiary,
                    style = MaterialTheme.typography.titleSmall
                )
            }

        }
    }

    @Composable
    fun ItemBottomSheet(
        onClicked: () -> Unit,
        textImportance: String
    ) {
        ExtendedFloatingActionButton(
            onClick = onClicked,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier
                .padding(15.dp)
        ) {
            Text(text = textImportance)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DateRow() {
        var openDialog by remember { mutableStateOf(false) }
        var date by remember { mutableStateOf("") }
        var checkedState by remember { mutableStateOf(currModel.deadlineData != "") }

        if(isCurrEditing) {
            date = currModel.deadlineData
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 25.dp, end = 25.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DateText(currentDate = date)

            Switch(
                checked = checkedState,
                onCheckedChange = { newValue ->
                    checkedState = newValue
                    openDialog = newValue

                    if(!newValue) {
                        date = ""
                        currModel.deadlineData = date
                    }
                },
                colors = SwitchDefaults.colors(
                    uncheckedThumbColor = Blue,
                    checkedThumbColor = Blue,
                    uncheckedTrackColor = MaterialTheme.colorScheme.secondary,
                    checkedTrackColor = MaterialTheme.colorScheme.secondary,
                    uncheckedBorderColor = Color.Transparent,
                    checkedBorderColor = Color.Transparent,
                )
            )

            if(openDialog){
                val datePickerState = rememberDatePickerState()

                DatePickerDialog(
                    onDismissRequest = { openDialog = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                var pickedDate = Calendar.getInstance()
                                pickedDate.timeInMillis = datePickerState.selectedDateMillis ?: 0
                                date = DateFormat.getDateInstance(DateFormat.LONG).format(pickedDate.time).dropLast(8)
                                currModel.deadlineData = date
                                openDialog = false
                            }
                        ) {
                            Text(
                                text = "OK",
                                color = Blue,
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    }
                ) {
                    DatePicker(
                        state = datePickerState,
                        dateFormatter = DatePickerFormatter(
                            "dd MMMM yyyy",
                            "dd MMMM yyyy",
                            "dd MMMM yyyy"
                        ),
                        colors = DatePickerDefaults.colors(
                            todayContentColor = Color.Red,
                            todayDateBorderColor = Color.Red,
                            selectedDayContainerColor = Blue,
                            selectedDayContentColor = White,
                            selectedYearContainerColor = Blue,
                            currentYearContentColor = Color.Red
                        )
                    )
                }
            }
        }
    }

    @Composable
    fun DateText(currentDate: String) { // Selected date Text
        Column {
            Text(
                text = "Сделать до",
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = currentDate,
                color = Blue,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun EditTaskTopBar() { // Top app bar
        TopAppBar(
            title = {},
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                scrolledContainerColor = MaterialTheme.colorScheme.onSecondary,
                navigationIconContentColor = MaterialTheme.colorScheme.onSecondary,
                actionIconContentColor = MaterialTheme.colorScheme.onSecondary
            ),
            navigationIcon = {
                IconButton(
                    onClick = {
                        parentFragment?.findNavController()?.navigateUp()
                    },
                    modifier = Modifier.padding(top = 20.dp, start = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close icon",
                        modifier = Modifier
                            .size(35.dp)
                    )
                }
            },
            actions = {
                // Save button
                TextButton(
                    onClick = {
                        if (isCurrEditing)
                            viewModel.updateTask(currModel)
                        else
                            viewModel.addTask(currModel)

                        parentFragment?.findNavController()?.navigateUp()
                    },
                    modifier = Modifier.padding(top = 20.dp, end = 10.dp)
                ) {
                    Text(
                        text = "СОХРАНИТЬ",
                        color = colorResource(id = R.color.blue),
                        fontSize = 20.sp
                    )
                }
            },
            windowInsets = WindowInsets(
                top = 0.dp,
                bottom = 0.dp
            )
        )
    }


    @Composable
    fun MTextField() {
        var value by remember {
            mutableStateOf("")
        }

        if(isCurrEditing)
            value = currModel.textCase

        // Text Field
        BasicTextField(
            value = value,
            onValueChange = { newText ->
                value = newText
                currModel.textCase = newText
            },
            modifier = Modifier
                .padding(top = 20.dp)
                .verticalScroll(ScrollState(0), false),
            textStyle = MaterialTheme.typography.bodyLarge,
            minLines = 5,
            decorationBox = { innerTextField ->
                Card(
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 20.dp)
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 10.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    content = {
                        Box(modifier = Modifier
                            .padding(all = 16.dp)
                        ) {
                            innerTextField()
                        }
                    })
            }
        )
    }


}